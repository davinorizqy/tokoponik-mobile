package com.example.tokoponik.restapi.models.product

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ProductPic(
    val id: Int,
    val product_id: Int,
    val path: String,
) : Parcelable
