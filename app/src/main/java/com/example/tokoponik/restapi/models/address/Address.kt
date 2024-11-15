package com.example.tokoponik.restapi.models.address

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Address (
    val id: Int,
    val user_id: Int,
    val receiver_name: String,
    val address: String,
    val note: String,
    val province: String,
    val district: String,
    val subdistrict: String,
    val post_code: String,
) : Parcelable