package com.example.tokoponik.restapi.models.user

data class userResponse(
    val status: Int,
    val message: String,
    val data: User
)
