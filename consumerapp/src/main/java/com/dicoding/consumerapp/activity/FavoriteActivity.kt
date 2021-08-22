package com.dicoding.consumerapp.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.consumerapp.R
import com.dicoding.consumerapp.adapter.ListUserAdapter
import com.dicoding.consumerapp.databinding.ActivityFavoriteBinding
import com.dicoding.consumerapp.db.FavoriteHelper
import com.dicoding.consumerapp.db.MappingHelper
import com.dicoding.consumerapp.model.User
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class FavoriteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoriteBinding
    private lateinit var adapter: ListUserAdapter
    private var user: User? = null

    companion object {
        private const val EXTRA_STATE = "EXTRA_STATE"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "User's Favorite"

        adapter = ListUserAdapter()
        adapter.notifyDataSetChanged()

        showSearchNotFound(true)
        showLoading(false)
        showRecyclerList()

        if (savedInstanceState == null) {
            // Proses pengambilan data
            loadUsersAsync()
            showRecyclerList()
        } else {
            val list = savedInstanceState.getParcelableArrayList<User>(EXTRA_STATE)
            if (list != null) {
                adapter.listFavorite = list
            }
        }

    }

    private fun showRecyclerList() {
        binding.rvUsers.visibility = View.VISIBLE
        binding.rvUsers.layoutManager = LinearLayoutManager(this)
        binding.rvUsers.setHasFixedSize(true)
        adapter = ListUserAdapter()
        binding.rvUsers.adapter = adapter

        adapter.setOnItemClickCallback(object : ListUserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: User, position: Int) {
                showSelectedUser(data, position)
            }
        })
    }

    private fun showSelectedUser(user: User, position: Int) {
        val moveDetailIntent= Intent(this@FavoriteActivity, DetailActivity::class.java)
        moveDetailIntent.putExtra(DetailActivity.EXTRA_USER, user)
        moveDetailIntent.putExtra(DetailActivity.EXTRA_POSITION, position)
        startActivity(moveDetailIntent)
    }

    private fun loadUsersAsync() {
        GlobalScope.launch(Dispatchers.Main) {
            showLoading(true)
            val userHelper = FavoriteHelper.getInstance(applicationContext)
            // ATURAN UTAMA: Membuat instance dan membuka koneksi pada metode onCreate()
            userHelper.open()
            val deferredUsers = async(Dispatchers.IO) {
                val cursor = userHelper.queryAll()
                MappingHelper.mapCursorToArrayList(cursor)
            }
            showLoading(false)
            val favoriteUser = deferredUsers.await()
            if (favoriteUser.size > 0) {
                showSearchNotFound(false)
                adapter.setData(favoriteUser)
                adapter.notifyDataSetChanged()
            } else {
                showSearchNotFound(true)
                adapter.setData(favoriteUser)
                adapter.notifyDataSetChanged()
                showSnackbarMessage("Tidak ada data saat ini")
            }
            userHelper.close()
            Log.d("myLog: ", favoriteUser.toString())
        }
    }

    private fun showSnackbarMessage(message: String) {
        Snackbar.make(binding.rvUsers, message, Snackbar.LENGTH_SHORT).show()
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun showSearchNotFound(state: Boolean) {
        if (state) {
            binding.imgSearch.visibility = View.VISIBLE
        } else {
            binding.imgSearch.visibility = View.GONE
        }
    }
}