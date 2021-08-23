package com.dicoding.githubuser.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.githubuser.R
import com.dicoding.githubuser.databinding.ActivityPreferenceBinding
import com.dicoding.githubuser.fragment.MyPreferanceFragment


class PreferenceActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPreferenceBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPreferenceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportFragmentManager.beginTransaction().add(R.id.setting_holder, MyPreferanceFragment()).commit()
    }

}