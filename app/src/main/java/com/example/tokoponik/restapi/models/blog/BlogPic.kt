package com.example.tokoponik.restapi.models.blog

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class BlogPic(
    val id: Int,
    val blog_id: Int,
    val pic_path: String
) : Parcelable
