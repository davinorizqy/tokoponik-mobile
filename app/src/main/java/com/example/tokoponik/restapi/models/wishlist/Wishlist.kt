package com.example.tokoponik.restapi.models.wishlist

import android.os.Parcelable
import com.example.tokoponik.restapi.models.product.Product
import com.example.tokoponik.restapi.models.user.User
import kotlinx.parcelize.Parcelize

@Parcelize
data class Wishlist(
    val id: Int,
    val user_id: Int,
    val product_id: Int,
    val user: User,
    val product: Product
) : Parcelable
