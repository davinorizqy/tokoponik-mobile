package com.example.tokoponik

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.Spinner
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Checkout : AppCompatActivity() {

    private lateinit var imgbtn_cart: ImageButton
    private lateinit var spinner_address: Spinner
    private lateinit var spinner_bank: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_checkout)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        imgbtn_cart = findViewById(R.id.imgbtn_to_cart)
        imgbtn_cart.setOnClickListener {
            val intent = Intent(this, Cart::class.java)
            startActivity(intent)
        }

        spinner_address = findViewById(R.id.spinner_address)
        spinner_bank = findViewById(R.id.spinner_bank)
    }
}