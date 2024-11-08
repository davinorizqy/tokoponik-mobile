package com.example.tokoponik

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Rating : AppCompatActivity() {

    private lateinit var imgBtnToTransactionDetail: ImageButton
    private lateinit var btnRating: Button
    private lateinit var imgBtnToCart: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_rating)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        imgBtnToTransactionDetail = findViewById(R.id.imgbtn_to_transactiondetail)
        imgBtnToTransactionDetail.setOnClickListener {
            val intent = Intent(this, TransactionDetail::class.java)
            startActivity(intent)
        }

        btnRating = findViewById(R.id.btn_rating)
        btnRating.setOnClickListener {
            val intent = Intent(this, TransactionDetail::class.java)
            startActivity(intent)
        }

        imgBtnToCart = findViewById(R.id.imgbtn_to_cart)
        imgBtnToCart.setOnClickListener {
            val intent = Intent(this, Cart::class.java)
            startActivity(intent)
        }
    }
}