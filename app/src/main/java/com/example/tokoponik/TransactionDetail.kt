package com.example.tokoponik

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tokoponik.helper.SessionManager
import com.example.tokoponik.restapi.adapter.TransactionDetailAdapter
import com.example.tokoponik.restapi.models.transacion.Transaction
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Locale

class TransactionDetail : AppCompatActivity() {

    private lateinit var imgBtnToTransaction: ImageButton
    private lateinit var btnToRate: Button
    private lateinit var imgBtnToCart: ImageButton
    private lateinit var btnToAddProof: Button

    private lateinit var tvNumber: TextView
    private lateinit var tvQty: TextView
    private lateinit var tvDate: TextView
    private lateinit var tvStatus: TextView
    private lateinit var productRecyclerView: RecyclerView
    private lateinit var tvReceiverName: TextView
    private lateinit var tvAddress: TextView
    private lateinit var tvBanknum: TextView
    private lateinit var tvTotal: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_transaction_detail)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val transaction = intent.getParcelableExtra<Transaction>("transaction")

        imgBtnToTransaction = findViewById(R.id.imgbtn_to_transaction)
        imgBtnToTransaction.setOnClickListener {
            val intent = Intent(this, Home::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            intent.putExtra("showTransactionFragment", true)
            startActivity(intent)
            finish()
        }

        btnToRate = findViewById(R.id.btn_to_rateform)
        btnToRate.setOnClickListener {
            val intent = Intent(this, Rating::class.java)
            startActivity(intent)
        }

        imgBtnToCart = findViewById(R.id.imgbtn_to_cart)
        imgBtnToCart.setOnClickListener {
            val intent = Intent(this, Cart::class.java)
            startActivity(intent)
        }

        btnToAddProof = findViewById(R.id.btn_to_addproof)
        btnToAddProof.setOnClickListener {
            val intent = Intent(this, AddProof::class.java)
            intent.putExtra("transaction", transaction)
            startActivity(intent)
        }

        tvNumber = findViewById(R.id.tv_number)
        tvQty = findViewById(R.id.tv_qty)
        tvDate = findViewById(R.id.tv_date)
        tvStatus = findViewById(R.id.tv_status)
        productRecyclerView = findViewById(R.id.transactiondetailRecyclerView)
        tvReceiverName = findViewById(R.id.tv_receiverName)
        tvAddress = findViewById(R.id.tv_address)
        tvBanknum = findViewById(R.id.tv_banknum)
        tvTotal = findViewById(R.id.tv_total)

        if (transaction != null) {
            Log.d("data", transaction.toString())

            tvNumber.text = "T-" + transaction.id.toString()
            tvQty.text = (transaction.transaction_detail.sumOf{ it.qty }).toString()

            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'", Locale.getDefault())
            val outputFormat = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
            val date = inputFormat.parse(transaction.created_at)
            tvDate.text = outputFormat.format(date)

            productRecyclerView.layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)
            val transactionDetailAdapter = TransactionDetailAdapter { detail ->
                Log.d("Item Clicked", detail.toString())
            }
            productRecyclerView.adapter = transactionDetailAdapter
            transactionDetailAdapter.submitList(transaction.transaction_detail)

            tvStatus.text = transaction.status.replaceFirstChar { it.uppercase() }
            tvReceiverName.text = transaction.address.receiver_name
            tvAddress.text = transaction.address.address + ", " + transaction.address.subdistrict + ", " + transaction.address.district + ", " + transaction.address.province + " - " + transaction.address.post_code

            val bank = transaction.bank.bank_name + " - " + transaction.bank.number + " (" + transaction.bank.owner_name + ")"
            tvBanknum.text = bank

            tvTotal.text = formatCurrency(transaction.grand_total)

            if (transaction.status != "pending") {
                btnToAddProof.visibility = View.GONE
            } else {
                btnToAddProof.visibility = View.VISIBLE
            }

            if (transaction.status != "selesai") {
                btnToRate.visibility = View.GONE
            } else {
                btnToRate.visibility = View.VISIBLE
            }
        } else {
            Log.e("TransactionDetail", "Transaction data is null")
        }
    }

    private fun formatCurrency(amount: Int): String {
        val numberFormat = NumberFormat.getCurrencyInstance(Locale("id", "ID"))
        numberFormat.maximumFractionDigits = 2
        numberFormat.minimumFractionDigits = 2
        val formattedPrice = numberFormat.format(amount).replace("Rp", "Rp. ")
        return formattedPrice
    }
}