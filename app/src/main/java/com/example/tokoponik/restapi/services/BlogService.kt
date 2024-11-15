package com.example.tokoponik.restapi.services

import com.example.tokoponik.restapi.models.blog.blogResponse
import retrofit2.Call
import retrofit2.http.GET

interface BlogService {
    @GET("auth/blogs")
    fun getAllBlogs() : Call<blogResponse>
}