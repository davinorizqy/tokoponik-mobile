package com.example.tokoponik

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
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
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Locale

class BlogDetail : AppCompatActivity() {

    private lateinit var imgbtn_to_blog: ImageButton
    private lateinit var tv_see_all_blogg: TextView
    private lateinit var blogRecyclerView: RecyclerView

    private lateinit var call: Call<blogResponse>
    private lateinit var sessionManager: SessionManager
    private lateinit var blogService: BlogService
    private lateinit var blogAdapter: BlogAdapter

    private lateinit var picBlog: ImageView
    private lateinit var tvTitle: TextView
    private lateinit var tvDate: TextView
    private lateinit var tvDesc: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_blog_detail)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        blogRecyclerView = findViewById(R.id.blogRecyclerView)
        blogAdapter = BlogAdapter { blog: Blog -> blogOnClick(blog) }
        blogRecyclerView.adapter = blogAdapter
        blogRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

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

        val blog: Blog? = intent.getParcelableExtra("blog")

        picBlog = findViewById(R.id.pic_blog)
        tvTitle = findViewById(R.id.tv_title)
        tvDate = findViewById(R.id.tv_date)
        tvDesc = findViewById(R.id.tv_desc)

        if (blog != null) {
            Log.d("Blog", blog.toString())

            Picasso.get().load(blog.blog_pics[0].pic_path).into(picBlog)

            tvTitle.text = blog.title
            tvDesc.text = blog.description

            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'", Locale.getDefault())
            val outputFormat = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
            val date = inputFormat.parse(blog.created_at)
            val formattedDate = outputFormat.format(date)

            tvDate.text = formattedDate
        }

        sessionManager = SessionManager(this)
        blogService = ApiClient.getBlogService(sessionManager)

        getBlogs()
    }

    private fun blogOnClick(blog: Blog) {
        val intent = Intent(this, ProductDetail::class.java)
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