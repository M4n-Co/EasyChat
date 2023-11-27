package com.example.easychat.chat.model

import com.google.firebase.Timestamp

data class ChatroomModel(
    val chatroomId : String? = null,
    val userIds : List<String?>? = null,
    var lastMessageTimestamp: Timestamp? = null,
    var lastMessageSenderId : String?= null,
    var lastMessage : String? = null
)