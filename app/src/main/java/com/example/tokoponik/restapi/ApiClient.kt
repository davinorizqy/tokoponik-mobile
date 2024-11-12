package com.example.tokoponik.restapi

import com.example.tokoponik.helper.SessionManager
import com.example.tokoponik.restapi.services.AddressService
import com.example.tokoponik.restapi.services.AuthService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    private const val BASE_URL = "http://10.0.2.2:8000/api/"

    private fun getOkHttpClient(session: SessionManager): OkHttpClient {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        return OkHttpClient.Builder()
            .addInterceptor(logging)
            .addInterceptor { chain ->
                val requestBuilder = chain.request().newBuilder()
                val token = session.getAuthToken()

                if (token != null) {
                    requestBuilder.addHeader("Authorization", "Bearer $token")
                }

                requestBuilder.addHeader("Accept", "application/json")
                chain.proceed(requestBuilder.build())
            }
            .build()
    }

    fun getRetrofit(session: SessionManager? = null): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(getOkHttpClient(session!!))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun getAuthService(session: SessionManager): AuthService {
        return getRetrofit(session).create(AuthService::class.java)
    }

    fun getAddressService(session: SessionManager): AddressService {
        return getRetrofit(session).create(AddressService::class.java)
    }
}