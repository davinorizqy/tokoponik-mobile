package com.example.tokoponik.restapi.services

import com.example.tokoponik.restapi.models.cart.cudResponse
import com.example.tokoponik.restapi.models.cart.getResponse
import com.example.tokoponik.restapi.models.cart.totalResponse
import retrofit2.Call
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface CartService {
    @GET("auth/carts")
    fun getUserCart() : Call<getResponse>

    @GET("auth/carts/total/all")
    fun getCartTotal() : Call<totalResponse>

    @FormUrlEncoded
    @POST("auth/carts/store")
    fun addToCart(
        @Field("product_id") product_id: Int,
        @Field("qty") qty: Int
    ) : Call<cudResponse>

    @DELETE("auth/carts/{id}/destroy")
    fun destroyCartItem(@Path("id") id : Int) : Call<cudResponse>
}