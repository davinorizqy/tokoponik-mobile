package com.example.tokoponik.restapi.models.transacion

data class processResponse (
    val status: Int,
    val message: String,
    val data: Transaction
)