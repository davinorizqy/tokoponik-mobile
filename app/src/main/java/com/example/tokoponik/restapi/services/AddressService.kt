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

// SEMUA ENDPOIN NANTI DITAMBAHIN "auth/" DI DEPANNYA PAS FUNGSI LOGIN UDAH JADI
interface AddressService {
    @GET("addresses/user/{user_id}")
    fun getUserAddress(@Path("user_id") user_id: Int): Call<getResponse>

    @FormUrlEncoded
    @POST("addresses/store")
    fun storeAddress(
//        @Body data: Address
        @Field("user_id") user_id: Int,
//        @Field("receiver_name") receiver_name: String,
        @Field("address") address: String,
        @Field("province") province: String,
        @Field("district") district: String,
        @Field("subdistrict") subdistrict: String,
        @Field("post_code") post_code: String,
        @Field("note") note: String,
    ) : Call<cudResponse>

    @FormUrlEncoded
    @POST("addresses/{id}/update")
    fun updateAddress(
//        @Body data: Address
        @Path("id") id: Int,
        @Field("id") idField: Int,
        @Field("user_id") user_id: Int,
//        @Field("receiver_name") receiver_name: String,
        @Field("address") address: String,
        @Field("note") note: String,
        @Field("province") province: String,
        @Field("district") district: String,
        @Field("subdistrict") subdistrict: String,
        @Field("post_code") post_code: String,
    ) : Call<cudResponse>

    @DELETE("addresses/{id}/destroy")
    fun destroyAddress(@Path("id") id: Int) : Call<cudResponse>
}