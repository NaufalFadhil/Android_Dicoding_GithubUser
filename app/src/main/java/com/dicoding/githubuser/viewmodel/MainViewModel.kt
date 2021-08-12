package com.dicoding.githubuser.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.githubuser.model.User
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpClient.log
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONArray
import org.json.JSONObject
import java.lang.Exception

class MainViewModel : ViewModel() {

    val listUsers = MutableLiveData<ArrayList<User>>()
    val detailUser = MutableLiveData<User>()
    val listFollowers = MutableLiveData<ArrayList<User>>()
    val listFollowing = MutableLiveData<ArrayList<User>>()

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

    fun setDetail(username: String?) {
        val url = "https://api.github.com/users/${username}"
        val client = AsyncHttpClient()
      //client.addHeader("Authorization", "token")
        client.addHeader("User-Agent", "request")
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray
            ) {
                val result = String(responseBody)
                try {
                    val responseObject = JSONObject(result)
                    log.d("response", responseObject.toString())

                    for (i in 0 until responseObject.length()) {
                        val username = responseObject.getString("login")
                        val name = responseObject.getString("name")
                        val avatar = responseObject.getString("avatar_url")
                        val followers = responseObject.getInt("followers")
                        val following = responseObject.getInt("following")
                        val repository = responseObject.getInt("public_repos")
                        val company = responseObject.getString("company")
                        val location = responseObject.getString("location")
                        val blog = responseObject.getString("blog")

                        val detailUserItem = User()
                        detailUserItem.avatar = avatar
                        detailUserItem.username = username
                        detailUserItem.name = name
                        detailUserItem.followers = followers
                        detailUserItem.following = following
                        detailUserItem.repository = repository
                        detailUserItem.location = location
                        detailUserItem.company = company
                        detailUserItem.blog = blog
                        log.d("output nihh", detailUserItem.toString())

                        detailUser.postValue(detailUserItem)
                    }
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

    fun getDetail(): LiveData<User> {
        return detailUser
    }

    fun setFollowers(username: String?) {
        val client = AsyncHttpClient()
      //client.addHeader("Authorization", "token <ReviewerTokenPlease>")
        client.addHeader("User-Agent", "request")
        val url = "https://api.github.com/users/${username}/followers"
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray
            ) {
                val listItems = ArrayList<User>()
                val result = String(responseBody)
                log.d("myResult", result)
                try {
                    val responseArray = JSONArray(result)
                    log.d("myResponseArray", responseArray.toString())
                    for (i in 0 until responseArray.length()) {
                        val responObjects = responseArray.getJSONObject(i)
                        log.d("myResponseObject", responObjects.toString())
                        val url = responObjects.getString("url")
                        val username = responObjects.getString("login")
                        val avatar = responObjects.getString("avatar_url")

                        val follower = User()
                        follower.avatar = avatar
                        follower.username = username
                        listItems.add(follower)
                        log.d("myTrySetUser", follower.toString())
                    }
                    listFollowers.postValue(listItems)
                } catch (e: Exception) {
                    e.printStackTrace()
                    Log.d("myExceptionSetUser", e.printStackTrace().toString())
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

    fun getFollowers(): LiveData<ArrayList<User>> {
        return listFollowers
    }

    fun setFollowing(username: String?) {
        val client = AsyncHttpClient()
      //client.addHeader("Authorization", "token <ReviewerTokenPlease>")
        client.addHeader("User-Agent", "request")
        val url = "https://api.github.com/users/${username}/following"
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray
            ) {
                val listItems = ArrayList<User>()
                val result = String(responseBody)
                log.d("myResult", result)
                try {
                    val responseArray = JSONArray(result)
                    log.d("myResponseArray", responseArray.toString())
                    for (i in 0 until responseArray.length()) {
                        val responObjects = responseArray.getJSONObject(i)
                        log.d("myResponseObject", responObjects.toString())
                        val url = responObjects.getString("url")
                        val username = responObjects.getString("login")
                        val avatar = responObjects.getString("avatar_url")

                        val following = User()
                        following.avatar = avatar
                        following.username = username
                        listItems.add(following)
                        log.d("myTrySetUser", following.toString())
                    }
                    listFollowing.postValue(listItems)
                } catch (e: Exception) {
                    e.printStackTrace()
                    Log.d("myExceptionSetUser", e.printStackTrace().toString())
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

    fun getFollowing(): LiveData<ArrayList<User>> {
        return listFollowing
    }


}