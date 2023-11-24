package com.example.easychat.utils

import android.content.Intent
import com.example.easychat.model.UserModel

class AndroidUtil {

    companion object{
        const val USERNAME = "USERNAME"
        const val PHONE = "PHONE"
        const val USERID = "USERID"
    }

    fun sendUser(intent: Intent, userModel: UserModel){
        intent.putExtra(USERNAME, userModel.username)
        intent.putExtra(PHONE, userModel.phone)
        intent.putExtra(USERID, userModel.userId)
    }

    fun getUSer(intent: Intent): UserModel {
        return UserModel(
            username = intent.getStringExtra(USERNAME),
            phone = intent.getStringExtra(PHONE),
            userId = intent.getStringExtra(USERID)
        )
    }
}