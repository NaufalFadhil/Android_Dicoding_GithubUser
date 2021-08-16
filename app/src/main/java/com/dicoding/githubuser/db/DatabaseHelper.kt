package com.dicoding.githubuser.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.dicoding.githubuser.db.DatabaseContract.FavoriteColumns.Companion.TABLE_NAME

internal class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "dbgithubuser"
        private const val DATABASE_VERSION = 1
        private const val SQL_CREATE_TABLE_FAVORITE = "CREATE TABLE $TABLE_NAME" +
                " (${DatabaseContract.FavoriteColumns._ID} INTEGER PRIMARY KEY AUTOINCREMENT," +
                " ${DatabaseContract.FavoriteColumns.AVATAR} TEXT NOT NULL," +
                " ${DatabaseContract.FavoriteColumns.USERNAME} TEXT NOT NULL)"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(SQL_CREATE_TABLE_FAVORITE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }
}