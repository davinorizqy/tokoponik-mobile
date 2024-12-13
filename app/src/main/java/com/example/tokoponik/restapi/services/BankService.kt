package com.example.tokoponik.restapi.services

import com.example.tokoponik.restapi.models.bank.bankResponse
import retrofit2.Call
import retrofit2.http.GET

interface BankService {
    @GET("auth/banks")
    fun getAllBank() : Call<bankResponse>
}