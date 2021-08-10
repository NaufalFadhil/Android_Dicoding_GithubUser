package com.dicoding.githubuser.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.githubuser.R
import com.dicoding.githubuser.User
import com.dicoding.githubuser.adapter.ListUserAdapter
import com.dicoding.githubuser.databinding.ActivityUserListBinding
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpClient.log
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONArray
import org.json.JSONObject
import java.lang.Exception

class UserListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserListBinding
    private val list = ArrayList<User>()

    companion object {
        private val TAG = UserListActivity::class.java.simpleName
    }

    private fun showSelectedUser(user: User) {
//        Toast.makeText(this, "You are choose ${user.username}", Toast.LENGTH_SHORT).show()
        val moveDetailIntent= Intent(this@UserListActivity, DetailActivity::class.java)
        moveDetailIntent.putExtra(DetailActivity.EXTRA_USER, user)
        startActivity(moveDetailIntent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rvUsers.setHasFixedSize(true)

        getListUsers()
    }

    private fun showRecyclerList() {
        binding.rvUsers.layoutManager = LinearLayoutManager(this)
        val listUserAdapter = ListUserAdapter(list)
        binding.rvUsers.adapter = listUserAdapter

        listUserAdapter.setOnItemClickCallback(object : ListUserAdapter.OnItemClickCallback{
            override fun onItemClicked(data: User) {
                showSelectedUser(data)
            }
        })
    }

    private fun getListUsers() {
        binding.progressBar.visibility = View.VISIBLE
        val client = AsyncHttpClient()
//        client.addHeader("Authorization", "token <TokenGithub>")
        client.addHeader("User-Agent", "request")
        val url = "https://api.github.com/search/users?q=naufalfadhil"
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray
            ) {
                binding.progressBar.visibility = View.INVISIBLE

                val listItems = ArrayList<User>()
                val result = String(responseBody)
                Log.d(TAG, result)
                try {
                    val responseObject = JSONObject(result)
                    val items = responseObject.getJSONArray("items")

                    for (i in 0 until items.length()) {
                        val item = items.getJSONObject(i)
                        val id = item.getInt("id")
                        val username = item.getString("login")
                        val avatar = item.getString("avatar_url")

                        val user = User()
                        user.id = id
                        user.avatar = avatar
                        user.username = username
                        listItems.add(user)
                    }
                    list.addAll(listItems)

                    showRecyclerList()
                } catch (e: Exception) {
                    Toast.makeText(this@UserListActivity, e.message, Toast.LENGTH_SHORT).show()
                    e.printStackTrace()
                }

            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?,
                error: Throwable?
            ) {
                binding.progressBar.visibility = View.INVISIBLE
                val errorMessage = when (statusCode) {
                    401 -> "$statusCode : Bad Request"
                    403 -> "$statusCode : Forbidden"
                    404 -> "$statusCode : Not Found"
                    else -> "$statusCode : ${error?.message}"
                }
                Toast.makeText(this@UserListActivity, errorMessage, Toast.LENGTH_SHORT).show()
            }
        })
    }
}