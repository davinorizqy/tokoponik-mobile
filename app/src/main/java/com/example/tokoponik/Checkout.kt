package com.example.tokoponik

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageButton
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.tokoponik.helper.SessionManager
import com.example.tokoponik.restapi.ApiClient
import com.example.tokoponik.restapi.models.address.Address
import com.example.tokoponik.restapi.models.address.getResponse
import com.example.tokoponik.restapi.models.bank.Bank
import com.example.tokoponik.restapi.models.bank.bankResponse
import com.example.tokoponik.restapi.models.transacion.processResponse
import com.example.tokoponik.restapi.services.AddressService
import com.example.tokoponik.restapi.services.BankService
import com.example.tokoponik.restapi.services.TransactionService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.NumberFormat
import java.util.Locale

class Checkout : AppCompatActivity() {

    private lateinit var imgbtn_cart: ImageButton
    private lateinit var spinner_address: Spinner
    private lateinit var spinner_bank: Spinner
    private lateinit var btn_buy: Button

    private  lateinit var tv_address: TextView
    private  lateinit var tv_detail: TextView
    private  lateinit var tv_note: TextView
    private  lateinit var tv_owner: TextView
    private  lateinit var tv_number: TextView
    private  lateinit var tv_total_amount: TextView
    private  lateinit var tv_shipment_price: TextView
    private  lateinit var tv_price_total: TextView

    private lateinit var callAddress: Call<getResponse>
    private lateinit var callBank: Call<bankResponse>
    private lateinit var callCheckout: Call<processResponse>
    private lateinit var sessionManager: SessionManager
    private lateinit var addressService: AddressService
    private lateinit var bankService: BankService
    private lateinit var transactionService: TransactionService

    private lateinit var addressData: List<Address>
    private lateinit var bankData: List<Bank>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_checkout)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val totalAmount = intent.getIntExtra("totalAmount", 0)

        tv_total_amount = findViewById(R.id.tv_total_amount)
        tv_shipment_price = findViewById(R.id.tv_shipment_price)
        tv_price_total = findViewById(R.id.tv_price_total)
        tv_total_amount.text = formatCurrency(totalAmount)
        tv_shipment_price.text = formatCurrency(10000)
        val priceTotal = totalAmount + 10000
        tv_price_total.text = formatCurrency(priceTotal)

        imgbtn_cart = findViewById(R.id.imgbtn_to_cart)
        imgbtn_cart.setOnClickListener {
            val intent = Intent(this, Cart::class.java)
            startActivity(intent)
        }

        spinner_address = findViewById(R.id.spinner_address)
        spinner_bank = findViewById(R.id.spinner_bank)

        tv_address = findViewById(R.id.tv_address)
        tv_detail = findViewById(R.id.tv_detail)
        tv_note = findViewById(R.id.tv_note)
        tv_owner = findViewById(R.id.tv_owner)
        tv_number = findViewById(R.id.tv_number)

        sessionManager = SessionManager(this)
        addressService = ApiClient.getAddressService(sessionManager)
        bankService = ApiClient.getBankService(sessionManager)
        transactionService = ApiClient.getTransactionService(sessionManager)

        getUserAddress()
        getBanks()

        spinner_address.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedAddress = addressData[position]
                updateAddressDetails(selectedAddress)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Do nothing or show a default state
            }
        }

        spinner_bank.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedBank = bankData[position]
                updateBankDetails(selectedBank)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Do nothing or show a default state
            }
        }

        btn_buy = findViewById(R.id.btn_buy)
        btn_buy.setOnClickListener {
            val selectedBankPosition = spinner_bank.selectedItemPosition
            val selectedAddressPosition = spinner_address.selectedItemPosition

            if (selectedBankPosition >= 0 && selectedAddressPosition >= 0) {
                val selectedBankId = bankData[selectedBankPosition].id
                val selectedAddressId = addressData[selectedAddressPosition].id

                AlertDialog.Builder(this)
                    .setTitle("Konfirmasi Checkout")
                    .setMessage("Apakah semua data sudah sesuai?")
                    .setPositiveButton("Konfirmasi") { dialog, _ ->
                        checkout(selectedBankId, selectedAddressId)
                        dialog.dismiss()
                    }
                    .setNegativeButton("Batal") { dialog, _ ->
                        dialog.dismiss()
                    }
                    .show()
            } else {
                Toast.makeText(this, "Please select a bank and address", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun formatCurrency(number: Int): String {
        val numberFormat = NumberFormat.getCurrencyInstance(Locale("id", "ID"))
        numberFormat.maximumFractionDigits = 2
        numberFormat.minimumFractionDigits = 2
        val formattedNumber = numberFormat.format(number).replace("Rp", "Rp. ")
        return formattedNumber
    }

    private fun checkout(bank_id: Int, address_id: Int) {
        callCheckout = transactionService.checkout(bank_id, address_id)
        callCheckout.enqueue(object : Callback<processResponse> {
            override fun onResponse(
                call: Call<processResponse>,
                response: Response<processResponse>
            ) {
                Log.d("Data Checkout", response.body()?.data.toString())
                Toast.makeText(applicationContext, "Pesanan berhasil dibuat", Toast.LENGTH_SHORT).show()
                Handler(Looper.getMainLooper()).postDelayed({
                    val intent = Intent(this@Checkout, Home::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                    intent.putExtra("showTransactionFragment", true)
                    startActivity(intent)
                    finish()
                }, 1000)
            }

            override fun onFailure(call: Call<processResponse>, t: Throwable) {
                Toast.makeText(applicationContext, t.localizedMessage, Toast.LENGTH_SHORT).show()
                Log.d("Error onFailure", t.toString())
            }

        })
    }

    private fun updateAddressDetails(address: Address) {
        tv_address.text = address.address
        tv_detail.text = "${address.subdistrict}, ${address.district}, ${address.province} - ${address.post_code}"
        tv_note.text = address.note
    }

    private fun updateBankDetails(bank: Bank) {
        tv_owner.text = bank.owner_name
        tv_number.text = bank.number
    }

    private fun getUserAddress() {
        callAddress = addressService.getUserAddress()
        callAddress.enqueue(object : Callback<getResponse> {
            override fun onResponse(
                call: Call<getResponse>,
                response: Response<getResponse>
            ) {
                if (response.isSuccessful) {
                    addressData = response.body()?.data ?: emptyList()
                    if (addressData.isNotEmpty()) {
                        val addressList = addressData.map { it.receiver_name }
                        val adapter = ArrayAdapter(
                            this@Checkout,
                            android.R.layout.simple_spinner_item,
                            addressList
                        )
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                        spinner_address.adapter = adapter
                    }
                } else {
                    Log.d("Not Success", response.toString())
                }
            }

            override fun onFailure(call: Call<getResponse>, t: Throwable) {
                Toast.makeText(applicationContext, t.localizedMessage, Toast.LENGTH_SHORT).show()
                Log.d("Error onFailure", t.localizedMessage)
            }
        })
    }

    private fun getBanks() {
        callBank = bankService.getAllBank()
        callBank.enqueue(object : Callback<bankResponse> {
            override fun onResponse(
                call: Call<bankResponse>,
                response: Response<bankResponse>
            ) {
                if (response.isSuccessful) {
                    bankData = response.body()?.data ?: emptyList()
                    if (bankData.isNotEmpty()) {
                        val bankList = bankData.map { it.bank_name }
                        val adapter = ArrayAdapter(
                            this@Checkout,
                            android.R.layout.simple_spinner_item,
                            bankList
                        )
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                        spinner_bank.adapter = adapter
                    }
                } else {
                    Log.d("Not Success", response.toString())
                }
            }

            override fun onFailure(call: Call<bankResponse>, t: Throwable) {
                Toast.makeText(applicationContext, t.localizedMessage, Toast.LENGTH_SHORT).show()
                Log.d("Error onFailure", t.localizedMessage)
            }
        })
    }
}