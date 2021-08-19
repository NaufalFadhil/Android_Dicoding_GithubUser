package com.dicoding.githubuser.activity

import android.content.ContentValues
import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.dicoding.githubuser.R
import com.dicoding.githubuser.adapter.SectionsPagerAdapter
import com.dicoding.githubuser.databinding.ActivityDetailBinding
import com.dicoding.githubuser.db.DatabaseContract
import com.dicoding.githubuser.db.FavoriteHelper
import com.dicoding.githubuser.model.User
import com.dicoding.githubuser.viewmodel.MainViewModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.loopj.android.http.AsyncHttpClient.log

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private lateinit var mainViewModel: MainViewModel
    private lateinit var favoriteHelper: FavoriteHelper

    private var userModel: User? = null
    private var position: Int = 0
    private var isFavorite = false

    companion object {
        const val EXTRA_USER = "extra_user"
        const val EXTRA_FAVORITE = "extra_favorite"
        const val EXTRA_POSITION = "extra_position"
        const val REQUEST_ADD = 100
        const val RESULT_ADD = 101
        const val REQUEST_DELETE = 200
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

        favoriteHelper = FavoriteHelper.getInstance(applicationContext)
        favoriteHelper.open()

        var user = intent.getParcelableExtra<User>(EXTRA_USER) as User
        position = intent.getIntExtra(EXTRA_POSITION, 0)
        userModel = User()

//        if (user != null) {
//            position = intent.getIntExtra(EXTRA_POSITION, 0)
//            isFavorite = false
//        } else {
//            user = User()
//        }

        log.d("MyUser", user.toString())
        log.d("MyPosition", position.toString())
        log.d("MyFavorite", isFavorite.toString())
        log.d("MyUserId", user.id.toString())

        mainViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(MainViewModel::class.java)
        mainViewModel.setDetail(user.username)
        mainViewModel.getDetail().observe(this, { detailUserItem ->
            if (detailUserItem != null) {
                with(binding) {
                    Glide.with(this@DetailActivity)
                        .load(detailUserItem.avatar)
                        .apply(RequestOptions().override(90, 90))
                        .into(imgAvatar)
                    log.d("apa yaa", detailUserItem.toString())
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

        tabLayoutAdapter(user.username)

        // Love button clicked
        cekFavorite(user)
        binding.btnFavorite.setOnClickListener {
            isFavorite = !isFavorite
            changeFavoriteIcon(isFavorite)
            setFavoriteList(isFavorite, user)
        }
    }

    private fun cekFavorite(user: User){
        val cursor: Cursor = favoriteHelper.queryById(user.id.toString())
        if (cursor.moveToNext()) {
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
            try {
                log.d("MyLog", "Mau insert nih")
                insertUser(user)
                changeFavoriteIcon(isFavorite)
                log.d("MyLog", "Behasil inser")
            } catch (e: Exception) {
                log.d("MyLog", "Gagal Memasukkan ke database")
            }
        } else {
            try {
                log.d("MyLog", "Mau ngapus")
                deleteUser(user)
                log.d("MyLog", "Behasil hapus")
            } catch (e: Exception) {
                log.d("MyLog", "Gagal Menghapus ke database")
            }
        }
    }

    private fun insertUser(user: User) {
        val values = ContentValues()
        values.put(DatabaseContract.FavoriteColumns._ID, user.id)
        values.put(DatabaseContract.FavoriteColumns.USERNAME, user.username)
        values.put(DatabaseContract.FavoriteColumns.AVATAR, user.avatar)
        favoriteHelper.insert(values)
        setResult(RESULT_ADD, intent)
    }

    private fun deleteUser(user: User) {
        favoriteHelper.deleteById(user.id.toString())
        val intent = Intent()
        intent.putExtra(EXTRA_POSITION, position)
        setResult(RESULT_DELETE, intent)
        isFavorite = isFavorite
        changeFavoriteIcon(isFavorite)
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

    override fun onBackPressed() {
        showAlertDialog(ALERT_DIALOG_CLOSE)
    }

}