package com.dicoding.githubuser.db

import android.provider.BaseColumns

internal class DatabaseContract {

    internal class FavoriteColumns : BaseColumns {
        companion object {
            const val TABLE_NAME = "favorite"
            const val _ID = "id"
            const val USERNAME = "username"
            const val AVATAR = "avatar"
        }
    }
}