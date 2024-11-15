package com.example.tokoponik.restapi.services

import com.example.tokoponik.restapi.models.user.userResponse
import retrofit2.Call
import retrofit2.http.GET

interface UserService {
    @GET("auth/users/id/info")
    fun getUserInfo() : Call<userResponse>
}