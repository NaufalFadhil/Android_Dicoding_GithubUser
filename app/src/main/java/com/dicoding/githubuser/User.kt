package com.dicoding.githubuser

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val avatar: Int,
    val name: String,
    val username: String,
    val company: String,
    val location: String,
    val followers: String,
    val following: String,
    val repository: String,
) : Parcelable