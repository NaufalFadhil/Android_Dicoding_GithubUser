package com.dicoding.consumerapp.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.consumerapp.R
import com.dicoding.consumerapp.databinding.ActivityPreferenceBinding
import com.dicoding.consumerapp.fragment.MyPreferanceFragment


class PreferenceActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPreferenceBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPreferenceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportFragmentManager.beginTransaction().add(R.id.setting_holder, MyPreferanceFragment()).commit()
    }

}