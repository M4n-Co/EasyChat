package com.example.easychat.searchUser.adapterAndHolder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.easychat.databinding.ItemUserToSearchBinding
import com.example.easychat.model.UserModel
import com.example.easychat.utils.FirebaseUtil

class SearchUserViewHolder(view : View):RecyclerView.ViewHolder(view){
    private val binding = ItemUserToSearchBinding.bind(view)

    fun bind(userModel: UserModel){

        binding.tvUsername.text = userModel.username

        if (userModel.userId == FirebaseUtil().currentUserId()){
            val username = userModel.username + " ( Yo )"
            binding.tvUsername.text = username
        }

        binding.tvPhoneNumber.text = userModel.phone

    }

}