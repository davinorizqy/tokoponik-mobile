package com.example.tokoponik

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class ProductDetail : AppCompatActivity() {

    private lateinit var imgbtnBackToProduct: ImageButton
    private lateinit var imgbtnToCart: ImageButton
    private lateinit var btnToReview: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_product_detail)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        imgbtnBackToProduct = findViewById(R.id.imgbtn_to_product)
        imgbtnBackToProduct.setOnClickListener {
            val intent = Intent(this, ViewProduct::class.java)
            startActivity(intent)
        }

        imgbtnToCart = findViewById(R.id.imgbtn_to_cart)
        imgbtnToCart.setOnClickListener {
            val intent = Intent(this, Cart::class.java)
            startActivity(intent)
        }

        btnToReview = findViewById(R.id.btn_to_review)
        btnToReview.setOnClickListener {
            val intent = Intent(this, ViewReview::class.java)
            startActivity(intent)
        }
    }
}