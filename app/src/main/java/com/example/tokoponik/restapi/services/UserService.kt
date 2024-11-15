package com.example.tokoponik.restapi.services

import com.example.tokoponik.restapi.models.user.userResponse
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface UserService {
    @GET("auth/users/id/info")
    fun getUserInfo() : Call<userResponse>

    @FormUrlEncoded
    @POST("auth/users/{id}/updateProfile")
    fun updateUserProfile(
        @Path("id") id: Int,
        @Field("name") name: String,
        @Field("phone_number") phone_number: String
    ) : Call<userResponse>
}