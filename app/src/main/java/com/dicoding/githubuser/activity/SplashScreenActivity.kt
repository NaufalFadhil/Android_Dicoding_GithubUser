package com.dicoding.githubuser.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.dicoding.githubuser.R

class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        var handler = Handler(mainLooper)
        handler.postDelayed({
            var splash = Intent(this@SplashScreenActivity, UserListActivity::class.java)
            startActivity(splash)
            finish()
        }, 1500)
    }
}