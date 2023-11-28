package com.example.easychat.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.easychat.R
import com.example.easychat.chat.activity.ChatActivity
import com.example.easychat.home.activity.HomeActivity
import com.example.easychat.login.activity.LoginActivity
import com.example.easychat.model.UserModel
import com.example.easychat.utils.AndroidUtil
import com.example.easychat.utils.FirebaseUtil

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        val screenSplash = installSplashScreen()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        screenSplash.setKeepOnScreenCondition{true}

        if(FirebaseUtil().isLoggedIn() && intent.extras != null){
            val userId = intent.getStringExtra("userId")!!
            FirebaseUtil().allUserCollectionReference().document(userId).get()
                .addOnCompleteListener {
                    if (it.isSuccessful){
                        val userModel = it.result.toObject(UserModel::class.java)!!

                        val mainIntent = Intent(this, HomeActivity::class.java)
                        mainIntent.flags = Intent.FLAG_ACTIVITY_NO_ANIMATION
                        startActivity(mainIntent)

                        val intent = Intent(this, ChatActivity::class.java)
                        AndroidUtil().sendUser(intent, userModel)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        startActivity(intent)
                        finish()
                    }
                }
        }else{
            var intent = Intent(this, LoginActivity::class.java)
            if(FirebaseUtil().isLoggedIn()){
                intent = Intent(this, HomeActivity::class.java)
            }
            startActivity(intent)
            finish()
        }


    }
}