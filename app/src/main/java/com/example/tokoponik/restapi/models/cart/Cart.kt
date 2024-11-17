package com.example.tokoponik.restapi.models.cart

import android.os.Parcelable
import com.example.tokoponik.restapi.models.product.Product
import kotlinx.parcelize.Parcelize

@Parcelize
data class Cart(
    val id: Int,
    val user_id: Int,
    val product_id: Int,
    val qty: Int,
    val status: String,
    val product: Product
) : Parcelable
