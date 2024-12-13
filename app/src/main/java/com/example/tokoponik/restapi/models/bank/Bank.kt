package com.example.tokoponik.restapi.models.bank

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Bank(
    val id: Int,
    val owner_name: String,
    val bank_name: String,
    val number: String
) : Parcelable
