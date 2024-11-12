package com.example.tokoponik.restapi

import com.example.tokoponik.restapi.services.AddressService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    private const val BASE_URL = "http://10.0.2.2:8000/api/"

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val addressService: AddressService by lazy {
        retrofit.create(AddressService::class.java)
    }
}