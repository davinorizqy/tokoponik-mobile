package com.example.tokoponik

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast

import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.tokoponik.restapi.ApiClient
import com.example.tokoponik.restapi.adapter.AddressAdapter
import com.example.tokoponik.restapi.models.address.cudResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddAddressForm : AppCompatActivity() {

    private lateinit var imgbtnBack: ImageButton
    private lateinit var btnAddAddress: Button
    private lateinit var etAddress: EditText
    private lateinit var etProvince: EditText
    private lateinit var etDistrict: EditText
    private lateinit var etSubdistrict: EditText
    private lateinit var etPostcode: EditText
    private lateinit var etNote: EditText

    private lateinit var call: Call<cudResponse>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_add_address_form)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        imgbtnBack = findViewById(R.id.imgbtn_back)
        btnAddAddress = findViewById(R.id.btn_add_address)

        etAddress = findViewById(R.id.et_address)
        etProvince = findViewById(R.id.et_province)
        etDistrict = findViewById(R.id.et_district)
        etSubdistrict = findViewById(R.id.et_subdistrict)
        etPostcode = findViewById(R.id.et_postcode)
        etNote = findViewById(R.id.et_note)

        imgbtnBack.setOnClickListener{
            val intent = Intent(this, ViewAddress::class.java)
            startActivity(intent)
        }
        btnAddAddress.setOnClickListener{
            storeAddress(1,
                etAddress.text.toString(),
                etProvince.text.toString(),
                etDistrict.text.toString(),
                etSubdistrict.text.toString(),
                etPostcode.text.toString(),
                etNote.text.toString())
        }
    }

    private fun storeAddress(user_id: Int, address: String, province: String, district: String, subdistrict: String, post_code: String, note: String) {
        call = ApiClient.addressService.storeAddress(
            user_id, address, province, district, subdistrict, post_code, note)

        call.enqueue(object : Callback<cudResponse> {
            override fun onResponse(call: Call<cudResponse>, response: Response<cudResponse>) {
                if (response.isSuccessful) {
                    Log.d("Data Address", response.body()?.data.toString())

                    Toast.makeText(applicationContext, "Alamat baru berhasil ditambahkan.", Toast.LENGTH_SHORT).show()

                    Handler(Looper.getMainLooper()).postDelayed({
                        val intent = Intent(this@AddAddressForm, ViewAddress::class.java)
                        startActivity(intent)
                        finish()
                    }, 3000)
                } else {
                    Log.d("Not Success", response.toString())
                }
            }

            override fun onFailure(call: Call<cudResponse>, t: Throwable) {
                Toast.makeText(applicationContext, t.localizedMessage, Toast.LENGTH_SHORT).show()
                Log.d("Error onFailure", t.localizedMessage)
            }
        })
    }
}
