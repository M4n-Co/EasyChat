package com.example.easychat.chat.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.easychat.R
import com.example.easychat.chat.model.ChatMessageModel
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions

class ChatAdapter(options : FirestoreRecyclerOptions<ChatMessageModel>) :
    FirestoreRecyclerAdapter<ChatMessageModel, ChatViewHolder>(options){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_chatroom_messages, parent, false)
        return ChatViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int, model: ChatMessageModel) {
        holder.bind(model)
    }
}