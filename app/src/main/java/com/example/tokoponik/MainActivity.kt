package com.example.tokoponik

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.tokoponik.helper.SessionManager

class MainActivity : AppCompatActivity() {
    private lateinit var session: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        session = SessionManager(this)
        val token = session.getAuthToken()
        Log.d("Token", token?: "Token is NULL")

        Handler(Looper.getMainLooper()).postDelayed({
            if (token.isNullOrEmpty()) {
                val intent = Intent(this, Login::class.java)
                startActivity(intent)
            } else {
                val intent = Intent(this, Home::class.java)
                startActivity(intent)
            }
            finish()
        }, 1000)
    }
}