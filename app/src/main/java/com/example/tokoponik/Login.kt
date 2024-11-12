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
import okhttp3.Request
import okio.Timeout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Login : AppCompatActivity() {

    private lateinit var tvToRegister: TextView
    private lateinit var et_username: EditText
    private lateinit var et_password: EditText
    private lateinit var btnLogin: Button
    private lateinit var session: SessionManager
    private lateinit var call: Call<authResponse>

    private lateinit var authService: AuthService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        et_username = findViewById(R.id.et_username)
        et_password = findViewById(R.id.et_password)

        btnLogin = findViewById(R.id.btn_login)
        tvToRegister = findViewById(R.id.tv_to_register)

        session = SessionManager(this)
        authService = ApiClient.getAuthService(session)

        btnLogin.setOnClickListener {
            login(
                et_username.text.toString(),
                et_password.text.toString()
            )
        }

        tvToRegister.setOnClickListener {
            val intent = Intent(this, Register::class.java)
            startActivity(intent)
        }
    }

    private fun login(username: String, password: String) {
        call = authService.login(username, password)

        call.enqueue(object : Callback<authResponse> {
            override fun onResponse(call: Call<authResponse>, response: Response<authResponse>) {
                if (response.isSuccessful) {
                    Log.d("Data User", response.body()?.data!!.user.toString())

                    Toast.makeText(applicationContext, "Autentikasi berhasil.", Toast.LENGTH_SHORT).show()

                    session.saveAuthToken(response.body()?.data!!.token)
                    Log.d("Session token", session.getAuthToken().toString())


                    Handler(Looper.getMainLooper()).postDelayed({
                        val intent = Intent(this@Login, Home::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                        intent.putExtra("showProfileFragment", true)
                        startActivity(intent)
                        finish()
                    }, 3000)
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