package com.dicoding.githubuser.activity

import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.dicoding.githubuser.R
import com.dicoding.githubuser.adapter.ListUserAdapter
import com.dicoding.githubuser.adapter.SectionsPagerAdapter
import com.dicoding.githubuser.databinding.ActivityDetailBinding
import com.dicoding.githubuser.db.DatabaseContract
import com.dicoding.githubuser.db.DatabaseContract.FavoriteColumns.Companion.CONTENT_URI
import com.dicoding.githubuser.db.MappingHelper
import com.dicoding.githubuser.model.User
import com.dicoding.githubuser.viewmodel.MainViewModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private lateinit var mainViewModel: MainViewModel
    private lateinit var uriWithId: Uri

    private var user: User? = null
    private var position: Int = 0
    private var isFavorite = false
    private var isAvail = false

    companion object {
        const val EXTRA_USER = "extra_user"
        const val EXTRA_POSITION = "extra_position"
        const val RESULT_ADD = 101
        const val RESULT_DELETE = 201

        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.followers,
            R.string.following
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        user = intent.getParcelableExtra(EXTRA_USER)

        if (user != null) {
            position = intent.getIntExtra(EXTRA_POSITION, 0)
            isAvail = true
        } else {
            user = User()
        }

        if (isAvail) {
            uriWithId = Uri.parse(DatabaseContract.FavoriteColumns.CONTENT_URI.toString() + "/" + user?.id)
            val cursor = contentResolver.query(uriWithId, null, null, null, null)

            if (cursor != null && cursor.moveToFirst()) {
                user = MappingHelper.mapCursorToObject(cursor)
                cursor.close()
            }
        }

        mainViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(MainViewModel::class.java)
        mainViewModel.setDetail(user?.username)
        mainViewModel.getDetail().observe(this, { detailUserItem ->
            if (detailUserItem != null) {
                with(binding) {
                    Glide.with(this@DetailActivity)
                        .load(detailUserItem.avatar)
                        .apply(RequestOptions().override(90, 90))
                        .into(imgAvatar)
                    tvName.text = detailUserItem.name
                    tvUsername.text = detailUserItem.username
                    tvLocation.text = detailUserItem.location
                    tvFollowers.text = detailUserItem.followers.toString()
                    tvFollowing.text = detailUserItem.following.toString()
                    tvRepository.text = detailUserItem.repository.toString()
                    tvBlog.text = detailUserItem.blog
                    tvCompany.text = detailUserItem.company
                }
            }
        })

        tabLayoutAdapter(user?.username)

        // Love button clicked
        cekFavorite(user!!)
        binding.btnFavorite.setOnClickListener {
            isFavorite = !isFavorite
            changeFavoriteIcon(isFavorite)
            setFavoriteList(isFavorite, user!!)
        }
    }

    private fun cekFavorite(user: User){
        uriWithId = Uri.parse(CONTENT_URI.toString() + "/" + user.id)
        val cursor = contentResolver.query(uriWithId, null, null, null, null)
        if (cursor != null && cursor.moveToNext()) {
            isFavorite = true
            changeFavoriteIcon(isFavorite)
        }
    }

    private fun changeFavoriteIcon(isFavorite: Boolean){
        if(isFavorite) {
            binding.btnFavorite.setImageResource(R.drawable.ic_baseline_favorite_24)
        } else {
            binding.btnFavorite.setImageResource(R.drawable.ic_baseline_not_favorite_24)
        }
    }

    private fun setFavoriteList(isFavorite: Boolean, user: User) {
        if(isFavorite) {
            insertUser(user)
            changeFavoriteIcon(isFavorite)
        } else {
            deleteUser()
        }
        val adapter = ListUserAdapter()
        adapter.notifyDataSetChanged()
    }

    private fun insertUser(user: User) {
        val values = ContentValues()
        values.put(DatabaseContract.FavoriteColumns._ID, user.id)
        values.put(DatabaseContract.FavoriteColumns.USERNAME, user.username)
        values.put(DatabaseContract.FavoriteColumns.AVATAR, user.avatar)
        contentResolver.insert(CONTENT_URI, values)
        setResult(RESULT_ADD, intent)
    }

    private fun deleteUser() {
        contentResolver.delete(uriWithId, null, null)
        val intent = Intent()
        intent.putExtra(EXTRA_POSITION, position)
        setResult(RESULT_DELETE, intent)

    }

    private fun tabLayoutAdapter(username: String?) {
        val sectionsPagerAdapter = SectionsPagerAdapter(this)
        sectionsPagerAdapter.username = username
        val viewPager: ViewPager2 = findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = findViewById(R.id.tabs)
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

        supportActionBar?.elevation = 0f
    }
}