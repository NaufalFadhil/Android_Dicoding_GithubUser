package com.dicoding.githubuser

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    var avatar: Int,
    var username: String,
    var name: String,
    var location: String,
    var followers: Int,
    var following: Int,
    var repository: Int,
    var company: String
) : Parcelable