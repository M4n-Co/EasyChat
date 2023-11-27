package com.example.easychat.chat.adapter

import android.view.View
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.easychat.chat.model.ChatMessageModel
import com.example.easychat.databinding.ItemChatroomMessagesBinding
import com.example.easychat.utils.FirebaseUtil

class ChatViewHolder(view: View) : RecyclerView.ViewHolder(view){
    private val binding = ItemChatroomMessagesBinding.bind(view)

    fun bind(chatMessageModel: ChatMessageModel){
        if (chatMessageModel.senderId == FirebaseUtil().currentUserId()){
            binding.tvMessageReceiver.isVisible = false
            binding.tvMessageSender.text = chatMessageModel.message
        }else{
            binding.tvMessageSender.isVisible = false
            binding.tvMessageReceiver.text = chatMessageModel.message
        }
    }
}