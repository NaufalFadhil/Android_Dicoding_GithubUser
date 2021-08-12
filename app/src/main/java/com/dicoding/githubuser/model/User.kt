package com.dicoding.githubuser.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    var url: String? = "",
    var avatar: String? = "",
    var username: String? = "",
    var name: String? = "",
    val company: String? = "",
    val location: String? = "",
    val followers: String? = "",
    val following: String? = "",
    val repository: String? = "",
) : Parcelable