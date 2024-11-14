package com.example.tokoponik.restapi.services

import com.example.tokoponik.restapi.models.product.getResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ProductService {
    @GET("products")
    fun getAllProduct() : Call<getResponse>

    @GET("products/limit/{limit}")
    fun getProductLimit(@Path("limit") limit: Int) : Call<getResponse>
}