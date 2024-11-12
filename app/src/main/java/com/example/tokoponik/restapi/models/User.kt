package com.example.tokoponik.restapi.models

data class User (
    val id: Int,
    val username: String,
    val password: String,
    val role: String,
    val name: String,
    val phone_number: String,
)