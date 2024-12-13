package com.example.tokoponik.restapi.models.transacion

import android.os.Parcelable
import com.example.tokoponik.restapi.models.product.Product
import kotlinx.parcelize.Parcelize

@Parcelize
data class Detail(
    val id: Int,
    val transaction_id: Int,
    val product_id: Int,
    val qty: Int,
    val product: Product
) : Parcelable
