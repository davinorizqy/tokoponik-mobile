package com.example.tokoponik

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class TransactionDetail : AppCompatActivity() {

    private lateinit var imgBtnToTransaction: ImageButton
    private lateinit var btnToRate: Button
    private lateinit var imgBtnToCart: ImageButton
    private lateinit var btnToAddProof: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_transaction_detail)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

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
            startActivity(intent)
        }

//        val transactionStatus = getTransactionStatus() // Replace with actual method to get status
//        if (transactionStatus == "pending") {
//            btnToAddProof.visibility = View.VISIBLE
//            btnToAddProof.setOnClickListener {
//                val intent = Intent(this, AddProof::class.java)
//                startActivity(intent)
//            }
//        } else {
//            btnToAddProof.visibility = View.GONE
//        }
    }
}