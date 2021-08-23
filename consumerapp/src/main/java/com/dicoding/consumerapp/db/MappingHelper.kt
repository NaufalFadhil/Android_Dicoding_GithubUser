package com.dicoding.consumerapp.db

import android.database.Cursor
import android.util.Log
import com.dicoding.consumerapp.model.User

// Membantu untuk mengkonversi Cursor ke ArrayList
object MappingHelper {
    fun mapCursorToArrayList(notesCursor: Cursor?): ArrayList<User> {
        val notesList = ArrayList<User>()

        // Fungsi apply digunakan untuk menyederhanakan kode yang berulang.
        notesCursor?.apply {
            while (moveToNext()) {
                val id = getInt(getColumnIndexOrThrow(DatabaseContract.FavoriteColumns._ID))
                val avatar = getString(getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.AVATAR))
                val username = getString(getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.USERNAME))
                notesList.add(User(id = id, avatar = avatar, username = username))
            }
        }
        return notesList
    }

    fun mapCursorToObject(notesCursor: Cursor?): User {
        var user = User()
        Log.d("MyCursor", notesCursor.toString())

        // Fungsi apply digunakan untuk menyederhanakan kode yang berulang.
        notesCursor?.apply {
            moveToFirst()
            val id = getInt(notesCursor.getColumnIndexOrThrow(DatabaseContract.FavoriteColumns._ID))
            val avatar = getString(notesCursor.getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.AVATAR))
            val username = getString(notesCursor.getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.USERNAME))
            user = User(id, avatar, username)
        }
        return user
    }
}