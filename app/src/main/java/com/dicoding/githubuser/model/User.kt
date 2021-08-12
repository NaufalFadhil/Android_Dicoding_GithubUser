package com.dicoding.githubuser.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    var url: String? = "",
    var avatar: String? = "",
    var username: String? = "",
    var name: String? = "",
    var company: String? = "",
    var location: String? = "",
    var followers: Int? = 0,
    var following: Int? = 0,
    var repository: Int? = 0,
) : Parcelable