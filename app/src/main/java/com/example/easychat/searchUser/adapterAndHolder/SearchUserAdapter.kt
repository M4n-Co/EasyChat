package com.example.easychat.searchUser.adapterAndHolder

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.easychat.R
import com.example.easychat.model.UserModel
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions

class SearchUserAdapter(options: FirestoreRecyclerOptions<UserModel>):
    FirestoreRecyclerAdapter<UserModel, SearchUserViewHolder>(options) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchUserViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_user_to_search, parent, false)
        return SearchUserViewHolder(view)
    }

    override fun onBindViewHolder(holder: SearchUserViewHolder, position: Int, model: UserModel) {
        holder.bind(model)
    }


}