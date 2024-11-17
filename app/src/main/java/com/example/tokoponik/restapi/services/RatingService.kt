package com.example.tokoponik.restapi.services

import com.example.tokoponik.restapi.models.rating.averageResponse
import com.example.tokoponik.restapi.models.rating.countResponse
import com.example.tokoponik.restapi.models.rating.ratingResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface RatingService {
    @GET("auth/ratings/product/{product_id}")
    fun getProductReview (
        @Path("product_id") product_id: Int
    ) : Call<ratingResponse>

    @GET("auth/ratings/product/{product_id}/{limit}")
    fun getProductReviewLimit (
        @Path("product_id") product_id: Int,
        @Path("limit") limit: Int
    ) : Call<ratingResponse>

    @GET("auth/ratings/get-average/{product_id}")
    fun getAverageRating (
        @Path("product_id") product_id: Int
    ) : Call<averageResponse>

    @GET("auth/ratings/get-review/{product_id}")
    fun getReviewCount (
        @Path("product_id") product_id: Int
    ) : Call<countResponse>
}