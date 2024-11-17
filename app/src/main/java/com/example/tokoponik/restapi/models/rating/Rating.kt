package com.example.tokoponik.restapi.models.rating

import android.os.Parcelable
import com.example.tokoponik.restapi.models.product.Product
import com.example.tokoponik.restapi.models.user.User
import kotlinx.parcelize.Parcelize

@Parcelize
data class Rating(
    val id: Int,
    val user_id: Int,
    val product_id: Int,
    val rating: Int,
    val comment: String,
    val created_at: String,
    val user: User,
    val product: Product,
) : Parcelable
