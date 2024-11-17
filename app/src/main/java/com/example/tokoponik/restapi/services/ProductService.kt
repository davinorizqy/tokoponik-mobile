package com.example.tokoponik.restapi.services

import com.example.tokoponik.restapi.models.product.productResponse
import com.example.tokoponik.restapi.models.product.productsResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ProductService {
    @GET("products")
    fun getAllProduct() : Call<productsResponse>

    @GET("products/{id}")
    fun getProductById(@Path("id") id: Int) : Call<productResponse>

    @GET("products/limit/{limit}")
    fun getProductLimit(@Path("limit") limit: Int) : Call<productsResponse>
}