package com.example.tokoponik.restapi.models.cart

data class getResponse(
    val status: Int,
    val message: String,
    val data: List<Cart>
)