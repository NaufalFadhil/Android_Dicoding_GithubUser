package com.dicoding.githubuser

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val avatar: Int,
    val username: String,
    val name: String,
    val location: String,
    val followers: Int,
    val following: Int,
    val repository: Int,
    val company: String
) : Parcelable