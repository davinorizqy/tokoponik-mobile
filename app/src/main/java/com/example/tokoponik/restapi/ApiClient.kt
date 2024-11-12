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

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun getOkHttpClient(session: SessionManager): OkHttpClient {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY

        return OkHttpClient.Builder()
            .addInterceptor(logging) // Logging for debugging
            .addInterceptor { chain ->
                val requestBuilder = chain.request().newBuilder()
                val token = session.getAuthToken()

                // Add Authorization header if token exists
                if (token != null) {
                    requestBuilder.addHeader("Authorization", "Bearer $token")
                }

                // Add Accept header
                requestBuilder.addHeader("Accept", "application/json")

                chain.proceed(requestBuilder.build())
            }
            .build()
    }

    val authService: AuthService by lazy {
        retrofit.create(AuthService::class.java)
    }

    val addressService: AddressService by lazy {
        retrofit.create(AddressService::class.java)
    }
}