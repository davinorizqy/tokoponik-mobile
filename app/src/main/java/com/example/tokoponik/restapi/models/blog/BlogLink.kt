package com.example.tokoponik.restapi.models.blog

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class BlogLink(
    val id: Int,
    val blog_id: Int,
    val link: String
) : Parcelable
