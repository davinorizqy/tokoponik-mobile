package com.example.tokoponik.restapi

import com.example.tokoponik.helper.SessionManager
import com.example.tokoponik.restapi.services.AddressService
import com.example.tokoponik.restapi.services.AuthService
import com.example.tokoponik.restapi.services.BlogService
import com.example.tokoponik.restapi.services.ProductService
import com.example.tokoponik.restapi.services.RatingService
import com.example.tokoponik.restapi.services.UserService
import com.example.tokoponik.restapi.services.WishlistService
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

    fun getUserService(session: SessionManager): UserService {
        return getRetrofit(session).create(UserService::class.java)
    }

    fun getAddressService(session: SessionManager): AddressService {
        return getRetrofit(session).create(AddressService::class.java)
    }

    fun getProductService(session: SessionManager): ProductService {
        return getRetrofit(session).create(ProductService::class.java)
    }

    fun getBlogService(session: SessionManager): BlogService {
        return getRetrofit(session).create(BlogService::class.java)
    }

    fun getRatingService(session: SessionManager): RatingService {
        return getRetrofit(session).create(RatingService::class.java)
    }

    fun getWishlistService(session: SessionManager): WishlistService {
        return getRetrofit(session).create(WishlistService::class.java)
    }
}