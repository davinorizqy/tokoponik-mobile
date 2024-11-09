package com.example.tokoponik

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class ViewProduct : AppCompatActivity() {

    private lateinit var imgbtn_home: ImageButton
    private lateinit var imgbtn_cart: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_view_product)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        imgbtn_home = findViewById(R.id.imgbtn_to_home)
        imgbtn_home.setOnClickListener {
            val intent = Intent(this, Home::class.java)
            startActivity(intent)
        }

        imgbtn_cart = findViewById(R.id.imgbtn_to_cart)
        imgbtn_cart.setOnClickListener {
            val intent = Intent(this, Cart::class.java)
            startActivity(intent)
        }
    }
}