package com.example.tokoponik.restapi.models.transacion

import android.os.Parcelable
import com.example.tokoponik.restapi.models.address.Address
import com.example.tokoponik.restapi.models.bank.Bank
import kotlinx.parcelize.Parcelize
import java.io.File

@Parcelize
data class Transaction(
    val id: Int,
    val user_id: Int,
    val bank_id: Int,
    val address_id: Int,
    val grand_total: Int,
    val status: String,
    val proof: String ? = null,
    val created_at: String,
    val transaction_detail: List<Detail>,
    val bank: Bank,
    val address: Address
) : Parcelable
