package com.example.tokoponik.restapi.models.product

data class getResponse (
    val status: Int,
    val message: String,
    val data: List<Product>
)