package com.example.tokoponik.restapi.models.address

data class getResponse (
    val status: Int,
    val message: String,
    val data: List<Address>
)