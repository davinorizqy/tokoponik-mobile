package com.example.tokoponik.restapi.models.transacion

data class transactionResponse(
    val status: Int,
    val message: String,
    val data: List<Transaction>
)
