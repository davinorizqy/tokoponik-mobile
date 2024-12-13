package com.example.tokoponik

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.tokoponik.helper.SessionManager
import com.example.tokoponik.restapi.ApiClient
import com.example.tokoponik.restapi.models.transacion.Transaction
import com.example.tokoponik.restapi.models.transacion.processResponse
import com.example.tokoponik.restapi.services.TransactionService
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response
import retrofit2.Call
import java.io.File

class AddProof : AppCompatActivity() {
    private lateinit var imgBtnToTransactionDetail: ImageButton
    private lateinit var btnSubmitProof: Button
    private lateinit var btnUploadProof: Button
    private lateinit var imgProof: ImageView

    private val PICK_IMAGE_REQUEST = 1
    private var selectedImageUri: Uri? = null

    private lateinit var call: Call<processResponse>
    private lateinit var sessionManager: SessionManager
    private lateinit var transactionService: TransactionService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_add_proof)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val transaction = intent.getParcelableExtra<Transaction>("transaction")
        Log.d("data", transaction.toString())

        imgProof = findViewById(R.id.img_proof) // Pastikan imgProof diinisialisasi

        imgBtnToTransactionDetail = findViewById(R.id.imgbtn_to_transaction_detail)
        imgBtnToTransactionDetail.setOnClickListener {
            val intent = Intent(this, TransactionDetail::class.java)
            intent.putExtra("transaction", transaction)
            startActivity(intent)
        }

        btnSubmitProof = findViewById(R.id.btn_submit_proof)
        btnSubmitProof.setOnClickListener {
            if (transaction != null) {
                uploadImage(transaction.id, selectedImageUri!!)
            }
        }

        btnUploadProof = findViewById(R.id.btn_upload)
        btnUploadProof.setOnClickListener {
            openGallery()
        }

        sessionManager = SessionManager(this)
        transactionService = ApiClient.getTransactionService(sessionManager)
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK).apply {
            type = "image/*"
            putExtra(Intent.EXTRA_MIME_TYPES, arrayOf("image/jpeg", "image/jpg", "image/png"))
        }
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            selectedImageUri = data.data
            Log.d("Foto", data.data.toString())
            imgProof.setImageURI(selectedImageUri)
        }
    }

    private fun getFilePathFromUri(uri: Uri): String? {
        val cursor = applicationContext.contentResolver.query(uri, arrayOf(android.provider.MediaStore.Images.Media.DATA), null, null, null)
        return cursor?.use {
            if (it.moveToFirst()) {
                val columnIndex = it.getColumnIndex(android.provider.MediaStore.Images.Media.DATA)
                it.getString(columnIndex)
            } else {
                null
            }
        }
    }

    private fun handleImageUri(uri: Uri): MultipartBody.Part? {
        val filePath = getFilePathFromUri(uri)
        Log.d("File Path", filePath.toString())
        filePath?.let {
            val file = File(filePath)
            if (file.exists()) {
                Log.d("File Exists", "File found: ${file.absolutePath}")
                val requestFile = RequestBody.create("image/*".toMediaTypeOrNull(), file)
                return MultipartBody.Part.createFormData("proof", file.name, requestFile)
            } else {
                Log.e("File Error", "File does not exist")
            }
        }
        return null
    }

    private fun uploadImage(transaction_id: Int, imageUri: Uri) {
        Log.d("File", imageUri.toString())
        val multipartBodyPart = handleImageUri(imageUri)
        if (multipartBodyPart != null) {
            call = transactionService.addProff(transaction_id, multipartBodyPart)

            call.enqueue(object : retrofit2.Callback<processResponse> {
                override fun onResponse(
                    call: Call<processResponse>,
                    response: retrofit2.Response<processResponse>
                ) {
                    if (response.isSuccessful) {
                        Log.d("Data Proof", response.body()?.data.toString())
                        Toast.makeText(applicationContext, "Bukti pembayaran telah dikirim.", Toast.LENGTH_SHORT).show()
                        Handler(Looper.getMainLooper()).postDelayed({
                            val intent = Intent(this@AddProof, Home::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                            intent.putExtra("showTransactionFragment", true)
                            startActivity(intent)
                            finish()
                        }, 1000)

                    } else {
                        Log.e("Not Success", "Error Code: ${response.code()}, Message: ${response.message()}")
                        Log.e("Response Body", response.errorBody()?.string() ?: "No error body")
                    }
                }

                override fun onFailure(call: Call<processResponse>, t: Throwable) {
                    Toast.makeText(applicationContext, t.localizedMessage, Toast.LENGTH_SHORT).show()
                    Log.d("Error onFailure", t.localizedMessage)
                }
            })
        } else {
            Log.e("Upload Error", "Failed to create MultipartBody.Part from image URI")
        }
    }
}
