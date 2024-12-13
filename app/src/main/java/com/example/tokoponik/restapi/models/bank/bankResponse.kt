package com.example.tokoponik.restapi.models.bank

data class bankResponse(
    val status: Int,
    val message: String,
    val data: List<Bank>
)
