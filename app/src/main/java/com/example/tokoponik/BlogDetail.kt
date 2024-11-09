package com.example.tokoponik

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class BlogDetail : AppCompatActivity() {

    private lateinit var imgbtn_to_blog: ImageButton
    private lateinit var tv_see_all_blogg: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_blog_detail)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        imgbtn_to_blog = findViewById(R.id.imgbtn_to_blog)
        imgbtn_to_blog.setOnClickListener {
            val intent = Intent(this, ViewBlog::class.java)
            startActivity(intent)
        }

        tv_see_all_blogg = findViewById(R.id.tv_see_all_blog)
        tv_see_all_blogg.setOnClickListener {
            val intent = Intent(this, ViewBlog::class.java)
            startActivity(intent)
        }
    }
}