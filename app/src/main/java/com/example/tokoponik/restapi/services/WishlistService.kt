package com.example.tokoponik.restapi.services

import com.example.tokoponik.restapi.models.wishlist.cdResponse
import com.example.tokoponik.restapi.models.wishlist.getResponse
import retrofit2.Call
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface WishlistService {
    @GET("auth/wishlists/id/user")
    fun getWishlist() : Call<getResponse>

    @FormUrlEncoded
    @POST("auth/wishlists/store")
    fun addToWishlist(
        @Field("product_id") product_id: Int
    ) : Call<cdResponse>

    @DELETE("auth/wishlists/{id}/destroy")
    fun removeFromWishlist(@Path("id") id: Int) : Call<cdResponse>
}