package com.example.tokoponik.restapi.models.rating

data class countResponse(
    val status: Int,
    val message: String,
    val review_count: Int
)
