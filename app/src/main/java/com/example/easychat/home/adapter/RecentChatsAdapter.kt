package com.example.easychat.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.easychat.R
import com.example.easychat.chat.model.ChatroomModel
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions

class RecentChatsAdapter (options: FirestoreRecyclerOptions<ChatroomModel>) :
    FirestoreRecyclerAdapter<ChatroomModel, RecentChatsViewHolder>(options){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecentChatsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_chat, parent, false)
        return RecentChatsViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: RecentChatsViewHolder,
        position: Int,
        model: ChatroomModel
    ) {
        holder.bind(model)
    }


}