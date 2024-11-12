package com.example.tokoponik.restapi.models.user

data class User (
    val id: Int,
    val username: String,
    val role: String,
    val name: String,
    val phone_number: String,
)