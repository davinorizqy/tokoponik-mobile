package com.example.tokoponik.restapi.services

import com.example.tokoponik.restapi.models.address.getResponse
import com.example.tokoponik.restapi.models.address.cudResponse
import retrofit2.Call
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface AddressService {
    @GET("auth/addresses/id/user")
    fun getUserAddress(): Call<getResponse>

    @FormUrlEncoded
    @POST("auth/addresses/store")
    fun storeAddress(
        @Field("receiver_name") receiver_name: String,
        @Field("address") address: String,
        @Field("province") province: String,
        @Field("district") district: String,
        @Field("subdistrict") subdistrict: String,
        @Field("post_code") post_code: String,
        @Field("note") note: String,
    ) : Call<cudResponse>

    @FormUrlEncoded
    @POST("auth/addresses/{id}/update")
    fun updateAddress(
        @Path("id") id: Int,
        @Field("receiver_name") receiver_name: String,
        @Field("address") address: String,
        @Field("province") province: String,
        @Field("district") district: String,
        @Field("subdistrict") subdistrict: String,
        @Field("post_code") post_code: String,
        @Field("note") note: String,
    ) : Call<cudResponse>

    @DELETE("auth/addresses/{id}/destroy")
    fun destroyAddress(@Path("id") id: Int) : Call<cudResponse>
}