package com.example.tokoponik.restapi.models.user

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User (
    val id: Int,
    val username: String,
    val role: String,
    val name: String,
    val phone_number: String,
) : Parcelable