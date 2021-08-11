package com.dicoding.githubuser.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.githubuser.model.User
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONObject
import java.lang.Exception

class MainViewModel : ViewModel() {

    val listUsers = MutableLiveData<ArrayList<User>>()

    fun setUser(query: String? = "naufalfad") {
        val client = AsyncHttpClient()
//        client.addHeader("Authorization", "token <ReviewerTokenPlease>")
        client.addHeader("User-Agent", "request")
        val url = "https://api.github.com/search/users?q=${query}"
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray
            ) {
                val listItems = ArrayList<User>()
                val result = String(responseBody)
                try {
                    val responseObject = JSONObject(result)
                    val items = responseObject.getJSONArray("items")

                    for (i in 0 until items.length()) {
                        val item = items.getJSONObject(i)
                        val url = item.getString("url")
                        val username = item.getString("login")
                        val avatar = item.getString("avatar_url")

                        val user = User()
                        user.url = url
                        user.avatar = avatar
                        user.username = username
                        listItems.add(user)
                    }
                    listUsers.postValue(listItems)
                } catch (e: Exception) {
                    Log.d("Exception", e.printStackTrace().toString())
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?,
                error: Throwable?
            ) {
                val errorMessage = when (statusCode) {
                    401 -> "$statusCode : Bad Request"
                    403 -> "$statusCode : Forbidden"
                    404 -> "$statusCode : Not Found"
                    else -> "$statusCode : ${error?.message}"
                }
                Log.d("OnFailure", errorMessage)
            }
        })
    }

    fun getUsers(): LiveData<ArrayList<User>> {
        return listUsers
    }
}