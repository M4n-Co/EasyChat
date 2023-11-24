package com.example.easychat.searchUser.adapterAndHolder

import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.easychat.R
import com.example.easychat.chat.activity.ChatActivity
import com.example.easychat.databinding.ItemUserToSearchBinding
import com.example.easychat.model.UserModel
import com.example.easychat.utils.AndroidUtil
import com.example.easychat.utils.FirebaseUtil

class SearchUserViewHolder(view : View):RecyclerView.ViewHolder(view){
    private val binding = ItemUserToSearchBinding.bind(view)

    fun bind(userModel: UserModel){

        binding.tvUsername.text = userModel.username

        if (userModel.userId == FirebaseUtil().currentUserId()){
            val username = "${userModel.username} ${binding.tvUsername.context.getString(R.string.me)}"
            binding.tvUsername.text = username
        }

        binding.tvPhoneNumber.text = userModel.phone

        itemView.setOnClickListener {
            val intent = Intent(it.context, ChatActivity::class.java)
            AndroidUtil().sendUser(intent, userModel)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            it.context.startActivity(intent)
        }
    }

}