package com.example.tokoponik.restapi.models.blog

import android.os.Parcelable
import com.example.tokoponik.restapi.models.user.User
import kotlinx.parcelize.Parcelize

@Parcelize
data class Blog(
    val id: Int,
    val user_id: Int,
    val title: String,
    val description: String,
    val created_at: String,
    val user: User,
    val blog_pics : List<BlogPic>,
) : Parcelable
