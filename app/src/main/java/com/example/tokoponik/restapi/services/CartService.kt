package com.example.tokoponik.restapi.services

import com.example.tokoponik.restapi.models.cart.getResponse
import com.example.tokoponik.restapi.models.cart.totalResponse
import retrofit2.Call
import retrofit2.http.GET

interface CartService {
    @GET("auth/carts")
    fun getUserCart() : Call<getResponse>

    @GET("auth/carts/all/total")
    fun getCartTotal() : Call<totalResponse>
}