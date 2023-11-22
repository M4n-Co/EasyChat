package com.example.easychat.login.model

import com.google.firebase.Timestamp

data class UserModel(
    var phone: String,
    var username: String,
    var createdTimestamp: Timestamp,
    //var userId: String,
    //var fcmToken: String
)
