package com.example.easychat.model

import com.google.firebase.Timestamp

data class UserModel(
    var phone: String? = null,
    var username: String? = null,
    var createdTimestamp: Timestamp? = null,
    var userId: String? = null,
    //var fcmToken: String
)
