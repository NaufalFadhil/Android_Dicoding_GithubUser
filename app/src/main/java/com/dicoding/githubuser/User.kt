package com.dicoding.githubuser

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    var id: Int = 0,
    var avatar: String? = "",
    var username: String? = "",
//    val company: String,
//    val location: String,
//    val followers: String,
//    val following: String,
//    val repository: String,
) : Parcelable