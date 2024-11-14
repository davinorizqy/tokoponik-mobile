package com.example.tokoponik

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.tokoponik.helper.SessionManager
import com.example.tokoponik.restapi.ApiClient
import com.example.tokoponik.restapi.models.user.authResponse
import com.example.tokoponik.restapi.services.AuthService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Register : AppCompatActivity() {

    private lateinit var tvToLogin: TextView
    private lateinit var btnRegister: Button

    private lateinit var et_name: EditText
    private lateinit var et_phonenumber: EditText
    private lateinit var et_username: EditText
    private lateinit var et_password: EditText
    private lateinit var et_confirm: EditText

    private lateinit var session: SessionManager
    private lateinit var call: Call<authResponse>
    private lateinit var authService: AuthService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        session = SessionManager(this)
        authService = ApiClient.getAuthService(session)

        et_name = findViewById(R.id.et_name)
        et_phonenumber = findViewById(R.id.et_phonenumber)
        et_username = findViewById(R.id.et_username)
        et_password = findViewById(R.id.et_password)
        et_confirm = findViewById(R.id.et_confirm)

        tvToLogin = findViewById(R.id.tv_to_login)
        btnRegister = findViewById(R.id.btn_register)

        tvToLogin.setOnClickListener{
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
        }
        btnRegister.setOnClickListener {
            if (et_password.text.toString() == et_confirm.text.toString()) {
                register(
                    et_name.text.toString(),
                    et_phonenumber.text.toString(),
                    et_username.text.toString(),
                    et_password.text.toString(),
                )
            } else {
                Toast.makeText(applicationContext, "Password yang dimasukan tidak sama!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun register(name: String, phone_number: String, username: String, password: String) {
        call = authService.register(name, username, password, "customer", phone_number)

        call.enqueue(object : Callback<authResponse> {
            override fun onResponse(call: Call<authResponse>, response: Response<authResponse>) {
                if (response.isSuccessful) {
                    Log.d("Data User", response.body()?.data!!.user.toString())

                    Toast.makeText(applicationContext, "Akun berhasil dibuat. Silahkan login!", Toast.LENGTH_SHORT).show()

                    Handler(Looper.getMainLooper()).postDelayed({
                        val intent = Intent(this@Register, Login::class.java)
                        startActivity(intent)
                        finish()
                    }, 1000)
                } else {
                    Log.d("Not Success", response.toString())
                }
            }

            override fun onFailure(call: Call<authResponse>, t: Throwable) {
                Toast.makeText(applicationContext, t.localizedMessage, Toast.LENGTH_SHORT).show()
                Log.d("Error onFailure", t.localizedMessage)
            }

        })
    }
}