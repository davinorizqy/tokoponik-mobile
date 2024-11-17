package com.example.tokoponik.restapi.models.rating

data class ratingResponse(
    val status: Int,
    val message: String,
    val data: List<Rating>
)
