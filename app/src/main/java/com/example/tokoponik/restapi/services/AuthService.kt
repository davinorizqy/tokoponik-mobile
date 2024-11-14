package com.example.tokoponik.restapi.services

import com.example.tokoponik.restapi.models.user.authResponse
import com.example.tokoponik.restapi.models.user.logoutResponse
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface AuthService {
    @FormUrlEncoded
    @POST("register")
    fun register(
        @Field("name") name: String,
        @Field("username") username: String,
        @Field("password") password: String,
        @Field("role") role: String,
        @Field("phone_number") phone_number: String,
    ) : Call<authResponse>

    @FormUrlEncoded
    @POST("login")
    fun login(
        @Field("username") username: String,
        @Field("password") password: String,
    ) : Call<authResponse>

    @POST("logout")
    fun logout() : Call<logoutResponse>
}