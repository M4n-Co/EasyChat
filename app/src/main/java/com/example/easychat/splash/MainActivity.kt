package com.example.easychat.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.easychat.R
import com.example.easychat.home.activity.HomeActivity
import com.example.easychat.login.activity.LoginActivity
import com.example.easychat.utils.FirebaseUtil

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        val screenSplash = installSplashScreen()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        screenSplash.setKeepOnScreenCondition{true}

        var intent = Intent(this, LoginActivity::class.java)
        if(FirebaseUtil().isLoggedIn()){
            intent = Intent(this, HomeActivity::class.java)
        }
        startActivity(intent)
        finish()

    }
}