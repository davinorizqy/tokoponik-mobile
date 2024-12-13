package com.example.tokoponik.restapi.services

import com.example.tokoponik.restapi.models.transacion.processResponse
import com.example.tokoponik.restapi.models.transacion.transactionResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface TransactionService {
    @GET("auth/transactions")
    fun getTransaction() : Call<transactionResponse>

    @FormUrlEncoded
    @POST("auth/transactions/checkout")
    fun checkout(
        @Field("bank_id") bank_id: Int,
        @Field("address_id") address_id: Int
    ) : Call<processResponse>

    @Multipart
    @POST("auth/transactions/{id}/add-proof")
    fun addProff(
        @Path("id") id: Int,
        @Part proof: MultipartBody.Part
    ): Call<processResponse>
}