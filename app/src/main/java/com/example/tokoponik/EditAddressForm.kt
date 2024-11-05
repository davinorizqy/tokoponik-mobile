package com.example.tokoponik

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class EditAddressForm : AppCompatActivity() {

    private lateinit var imgbtnBack: ImageButton
    private lateinit var btnUpdateAddress: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_edit_address_form)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        imgbtnBack = findViewById(R.id.imgbtn_back)
        btnUpdateAddress = findViewById(R.id.btn_update_address)
        imgbtnBack.setOnClickListener{
            val intent = Intent(this, ViewAddress::class.java)
            startActivity(intent)
        }
        btnUpdateAddress.setOnClickListener{
            val intent = Intent(this, ViewAddress::class.java)
            startActivity(intent)
        }
    }
}