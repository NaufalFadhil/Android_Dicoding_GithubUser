package com.dicoding.consumerapp.activity

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.consumerapp.R
import com.dicoding.consumerapp.model.User
import com.dicoding.consumerapp.adapter.ListUserAdapter
import com.dicoding.consumerapp.databinding.ActivityUserListBinding
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import com.dicoding.consumerapp.viewmodel.MainViewModel

class UserListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserListBinding
    private lateinit var adapter: ListUserAdapter
    private lateinit var mainViewModel: MainViewModel

    private fun showSelectedUser(user: User, position: Int) {
        val moveDetailIntent= Intent(this@UserListActivity, DetailActivity::class.java)
        moveDetailIntent.putExtra(DetailActivity.EXTRA_USER, user)
        startActivity(moveDetailIntent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Github Consumer"

        adapter = ListUserAdapter()
        adapter.notifyDataSetChanged()

        showSearchNotFound(true)
        showLoading(false)
        showRecyclerList()

        mainViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
            MainViewModel::class.java)
        mainViewModel.getUsers().observe(this, {
            User -> if (User.isEmpty()) {
                        showLoading(false)
                        showRecyclerView(false)
                        showSearchNotFound(true)
                    } else if (User != null) {
                        showLoading(false)
                        adapter.setData(User)
                    }
        })
    }

    private fun showRecyclerList() {
        binding.rvUsers.layoutManager = LinearLayoutManager(this)
        binding.rvUsers.adapter = adapter

        adapter.setOnItemClickCallback(object : ListUserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: User, position: Int) {
                showSelectedUser(data, position)
            }
        })
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

    private fun showRecyclerView(state: Boolean) {
        if (state) {
            binding.rvUsers.visibility = View.VISIBLE
        } else {
            binding.rvUsers.visibility = View.INVISIBLE
        }
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu?.findItem(R.id.search)?.actionView as SearchView

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = resources.getString((R.string.search_hint))
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return if (newText.isEmpty()) {
                    showSearchNotFound(true)
                    showLoading(false)
                    showRecyclerView(false)
                    true
                } else {
                    showLoading(true)
                    showSearchNotFound(false)
                    showRecyclerView(true)
                    mainViewModel.setUser(newText)
                    true
                }
            }
        })
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.settings -> {
//                val mIntent = Intent(Settings.ACTION_LOCALE_SETTINGS)
                val prefIntent = Intent(this, PreferenceActivity::class.java)
                startActivity(prefIntent)
            }
            R.id.favorite -> {
                val favIntent = Intent(this, FavoriteActivity::class.java)
                startActivity(favIntent)
            }
            else -> return true
        }
        return super.onOptionsItemSelected(item)
    }
}