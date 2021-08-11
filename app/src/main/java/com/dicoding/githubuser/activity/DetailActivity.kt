package com.dicoding.githubuser.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.dicoding.githubuser.R
import com.dicoding.githubuser.model.User
import com.dicoding.githubuser.adapter.SectionsPagerAdapter
import com.dicoding.githubuser.databinding.ActivityDetailBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import de.hdodenhof.circleimageview.CircleImageView

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

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

        val user = intent.getParcelableExtra<User>(EXTRA_USER) as User

        val imgAvatar: CircleImageView = binding.imgAvatar
        val tvUsername: TextView = binding.tvUsername
//        val tvFollowers: TextView = binding.tvFollowers
//        val tvFollowing: TextView = binding.tvFollowing
//        val tvLocation: TextView = binding.tvLocation
//        val tvRepository: TextView = binding.tvRepository

        Glide.with(this@DetailActivity)
            .load(user.avatar)
            .apply(RequestOptions().override(90,90))
            .into(imgAvatar)
//        tvName.text = user.name
        tvUsername.text = user.username
//        tvLocation.text = user.location
//        tvFollowers.text = user.followers.toString()
//        tvFollowing.text = user.following.toString()
//        tvRepository.text = user.repository.toString()

        val sectionsPagerAdapter = SectionsPagerAdapter(this)
        val viewPager: ViewPager2 = findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = findViewById(R.id.tabs)
        TabLayoutMediator(tabs, viewPager) {
            tab, position -> tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

        supportActionBar?.elevation = 0f
    }
}