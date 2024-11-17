package com.example.tokoponik.restapi.models.wishlist

data class getResponse(
    val status: Int,
    val message: String,
    val data: List<Wishlist>
)
