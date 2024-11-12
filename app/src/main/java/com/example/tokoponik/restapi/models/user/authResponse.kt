package com.example.tokoponik.restapi.models.user

import com.example.tokoponik.restapi.models.address.Address

data class authResponse(
    val status: Int,
    val message: String,
    val data: dataLogin
)
