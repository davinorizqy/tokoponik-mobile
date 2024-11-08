package com.example.tokoponik

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Checkout : AppCompatActivity() {

    private lateinit var imgbtn_cart: ImageButton
    private lateinit var tv_change_address: TextView
    private lateinit var tv_change_bank: TextView

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

        tv_change_address = findViewById(R.id.tv_change_address)
        tv_change_address.setOnClickListener {
            val intent = Intent(this, ViewAddress::class.java)
            startActivity(intent)
        }

        tv_change_bank = findViewById(R.id.tv_change_bank)
        tv_change_bank.setOnClickListener {
            val intent = Intent(this, ViewBank::class.java)
            startActivity(intent)
        }
    }
}