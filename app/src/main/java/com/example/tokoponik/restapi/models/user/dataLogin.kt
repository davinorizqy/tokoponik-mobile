package com.example.tokoponik.restapi.models.user

import com.example.tokoponik.restapi.models.address.Address

data class dataLogin(
    val token: String,
    val user: User,
)
