package com.example.tokoponik

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class ViewAddress : AppCompatActivity() {

    private lateinit var btnBackToProfileFragment: Button
    private lateinit var btnAddAddress: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_view_address)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        btnBackToProfileFragment = findViewById(R.id.btn_back_to_profile)
        btnAddAddress = findViewById(R.id.fab_add_address)

        btnBackToProfileFragment.setOnClickListener {
            val intent = Intent(this, Home::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            intent.putExtra("showProfileFragment", true)
            startActivity(intent)
            finish()
        }
        btnAddAddress.setOnClickListener {
            val intent = Intent(this, AddAddressForm::class.java)
            startActivity(intent)
        }
    }
}