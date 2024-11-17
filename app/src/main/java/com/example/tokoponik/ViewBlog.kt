package com.example.tokoponik

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tokoponik.helper.SessionManager
import com.example.tokoponik.restapi.ApiClient
import com.example.tokoponik.restapi.adapter.BlogAdapter
import com.example.tokoponik.restapi.models.blog.Blog
import com.example.tokoponik.restapi.models.blog.blogResponse
import com.example.tokoponik.restapi.services.BlogService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ViewBlog : AppCompatActivity() {

    private lateinit var imgbtn_home: ImageButton
    private lateinit var blogRecyclerView: RecyclerView

    private lateinit var call: Call<blogResponse>
    private lateinit var sessionManager: SessionManager
    private lateinit var blogService: BlogService
    private lateinit var blogAdapter: BlogAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_view_blog)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        blogRecyclerView = findViewById(R.id.blogRecyclerView)
        blogAdapter = BlogAdapter { blog: Blog -> blogOnClick(blog) }
        blogRecyclerView.adapter = blogAdapter
        blogRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        imgbtn_home = findViewById(R.id.imgbtn_to_home)
        imgbtn_home.setOnClickListener {
            val intent = Intent(this, Home::class.java)
            startActivity(intent)
        }

        sessionManager = SessionManager(this)
        blogService = ApiClient.getBlogService(sessionManager)

        getBlogs()
    }

    private fun blogOnClick(blog: Blog) {
        val intent = Intent(this, BlogDetail::class.java)
        intent.putExtra("blog", blog)
        startActivity(intent)
    }

    private fun getBlogs () {
        call = blogService.getAllBlogs()
        call.enqueue(object : Callback<blogResponse> {
            override fun onResponse(
                call: Call<blogResponse>,
                response: Response<blogResponse>
            ) {
                if (response.isSuccessful) {
                    Log.d("Data Blog", response.body()?.data.toString())
                    blogAdapter.submitList(response.body()?.data)
                    blogAdapter.notifyDataSetChanged()
                } else {
                    Log.d("Not Success", response.toString())
                }
            }

            override fun onFailure(call: Call<blogResponse>, t: Throwable) {
                Toast.makeText(applicationContext, t.localizedMessage, Toast.LENGTH_SHORT).show()
                Log.d("Error onFailure", t.localizedMessage)
            }
        })
    }
}