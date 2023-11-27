package com.example.easychat.home.adapter

import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.easychat.R
import com.example.easychat.chat.activity.ChatActivity
import com.example.easychat.chat.model.ChatroomModel
import com.example.easychat.databinding.ItemChatBinding
import com.example.easychat.model.UserModel
import com.example.easychat.utils.AndroidUtil
import com.example.easychat.utils.FirebaseUtil

class RecentChatsViewHolder (view : View) : RecyclerView.ViewHolder(view){

    private val binding = ItemChatBinding.bind(view)

    fun bind(chatroomModel: ChatroomModel){

        FirebaseUtil().getOtherUserFromChatroom(chatroomModel.userIds!!)
            .get().addOnCompleteListener {task ->
                if (task.isSuccessful){

                    val lastMessageSentByMe = chatroomModel.lastMessageSenderId == FirebaseUtil().currentUserId()

                    val otherUserModel = task.result.toObject(UserModel::class.java)!!

                    binding.tvUsername.text = otherUserModel.username

                    if (lastMessageSentByMe){
                        val myLastMessage = binding.tvLastMessage.context.getString(R.string.me)+" "+chatroomModel.lastMessage
                        binding.tvLastMessage.text = myLastMessage
                    }else{
                        binding.tvLastMessage.text = chatroomModel.lastMessage
                    }

                    binding.tvTime.text = FirebaseUtil().getTime(chatroomModel.lastMessageTimestamp!!)

                    itemView.setOnClickListener {
                        val intent = Intent(it.context, ChatActivity::class.java)
                        AndroidUtil().sendUser(intent, otherUserModel)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        it.context.startActivity(intent)
                    }

                }
            }

    }
}