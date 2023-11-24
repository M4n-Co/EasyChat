package com.example.easychat.chat.model

import com.google.firebase.Timestamp

data class ChatMessageModel(
    val message : String? = null,
    val senderId: String? = null,
    val lastMessageTimestamp: Timestamp? = null
)