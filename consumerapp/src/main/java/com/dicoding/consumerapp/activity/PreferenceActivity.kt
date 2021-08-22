package com.dicoding.githubuser.activity

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.consumerapp.R
import com.dicoding.consumerapp.databinding.ActivityDetailBinding
import com.dicoding.consumerapp.databinding.ActivityPreferenceBinding
import com.dicoding.consumerapp.fragment.MyPreferanceFragment
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpClient.log


class PreferenceActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPreferenceBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPreferenceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportFragmentManager.beginTransaction().add(R.id.setting_holder, MyPreferanceFragment()).commit()
    }

}