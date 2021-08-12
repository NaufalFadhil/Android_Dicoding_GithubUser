package com.dicoding.githubuser.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.dicoding.githubuser.R
import com.dicoding.githubuser.adapter.ListUserAdapter
import com.dicoding.githubuser.model.User
import com.dicoding.githubuser.adapter.SectionsPagerAdapter
import com.dicoding.githubuser.databinding.ActivityDetailBinding
import com.dicoding.githubuser.fragment.FollowersFragment
import com.dicoding.githubuser.viewmodel.MainViewModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.loopj.android.http.AsyncHttpClient
import de.hdodenhof.circleimageview.CircleImageView
import kotlin.math.log

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private lateinit var mainViewModel: MainViewModel
    private lateinit var adapter: ListUserAdapter

    private var dataUser: User? = null

    companion object {
        const val EXTRA_USER = "extra_user"

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

        val url = intent.getStringExtra("url")
        val fragment = FollowersFragment.newInstance(url)

        val imgAvatar: CircleImageView = binding.imgAvatar
        val tvUsername: TextView = binding.tvUsername
        val tvName: TextView = binding.tvName
        val tvFollowers: TextView = binding.tvFollowers
        val tvFollowing: TextView = binding.tvFollowing
        val tvLocation: TextView = binding.tvLocation


        mainViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
            MainViewModel::class.java)

        val user = intent.getParcelableExtra<User>(EXTRA_USER) as User
        mainViewModel.setDetail(user.username)

//        mainViewModel.getDetails().observe(this, {
//                detailUserItem -> if (detailUserItem != null) {
//                with(binding) {
//                    Glide.with(this@DetailActivity)
//                        .load(user.avatar)
//                        .apply(RequestOptions().override(90,90))
//                        .into(imgAvatar)
//                    tvName.text = detailUserItem.name
//                    tvUsername.text = detailUserItem.username
//                    tvLocation.text = detailUserItem.location
//                    tvFollowers.text = detailUserItem.followers.toString()
//                    tvFollowing.text = detailUserItem.following.toString()
////                    tvRepository.text = user.repository.toString()
//                }
//
//            }
//        })



//        val tvRepository: TextView = binding.tvRepository

        tabLayoutAdapter(user.username)

    }

    private fun tabLayoutAdapter(username: String?) {
        val sectionsPagerAdapter = SectionsPagerAdapter(this)
        sectionsPagerAdapter.username = username
        val viewPager: ViewPager2 = findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = findViewById(R.id.tabs)
        TabLayoutMediator(tabs, viewPager) {
                tab, position -> tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

        supportActionBar?.elevation = 0f
    }

//    private fun showLoading(state: Boolean) {
//        if (state) {
//            binding.progressBar.visibility = View.VISIBLE
//        } else {
//            binding.progressBar.visibility = View.GONE
//        }
//    }
}