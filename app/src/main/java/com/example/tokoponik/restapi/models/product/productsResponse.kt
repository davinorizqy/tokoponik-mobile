package com.example.tokoponik.restapi.models.product

data class productsResponse (
    val status: Int,
    val message: String,
    val data: List<Product>
)