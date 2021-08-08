package com.dicoding.githubuser.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.dicoding.githubuser.User
import com.dicoding.githubuser.databinding.ActivityDetailBinding
import de.hdodenhof.circleimageview.CircleImageView

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    companion object {
        const val EXTRA_USER = "extra_user"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val user = intent.getParcelableExtra<User>(EXTRA_USER) as User
        val cimAvatar: CircleImageView = binding.imgAvatar
        val tvName: TextView = binding.tvName
        val tvUsername: TextView = binding.tvUsername
        val tvFollowers: TextView = binding.tvFollowers
        val tvFollowing: TextView = binding.tvFollowing
        val tvLocation: TextView = binding.tvLocation
        val tvRepository: TextView = binding.tvRepository

//        cimAvatar.setImageResource(user.avatar)
        tvName.text = user.name
//        tvUsername.text = user.username
//        tvLocation.text = user.location
        tvFollowers.text = user.followers.toString()
        tvFollowing.text = user.following.toString()
//        tvRepository.text = user.repository.toString()

    }
}