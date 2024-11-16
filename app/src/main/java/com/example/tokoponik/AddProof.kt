package com.example.tokoponik

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class AddProof : AppCompatActivity() {
    private lateinit var imgBtnToTransactionDetail: ImageButton
    private lateinit var btnSubmitProof: Button
    private lateinit var btnUploadProof: Button
    private lateinit var imgProof: ImageView

    private val PICK_IMAGE_REQUEST = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_add_proof)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        imgBtnToTransactionDetail = findViewById(R.id.imgbtn_to_transaction_detail)
        imgBtnToTransactionDetail.setOnClickListener {
            val intent = Intent(this, TransactionDetail::class.java)
            startActivity(intent)
        }

        btnSubmitProof = findViewById(R.id.btn_submit_proof)
        btnSubmitProof.setOnClickListener {
            val intent = Intent(this, TransactionDetail::class.java)
            startActivity(intent)
        }

        btnUploadProof = findViewById(R.id.btn_upload)
        btnUploadProof.setOnClickListener {
            openGallery()
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            val selectedImageUri: Uri? = data.data
            imgProof.setImageURI(selectedImageUri)
        }
    }
}