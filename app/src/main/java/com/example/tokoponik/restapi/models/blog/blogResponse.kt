package com.example.tokoponik.restapi.models.blog

data class blogResponse(
    val status: Int,
    val message: String,
    val data: List<Blog>
)
