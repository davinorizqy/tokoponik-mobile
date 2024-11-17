package com.example.tokoponik.restapi.models.product

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Product (
    val id: Int,
    val name: String,
    val price: Int,
    val description: String,
    val type: String,
    val average_rating: Int,
    val product_pics: List<ProductPic>,
) : Parcelable