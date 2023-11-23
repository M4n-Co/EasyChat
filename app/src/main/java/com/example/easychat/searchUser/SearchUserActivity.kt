package com.example.easychat.searchUser

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.easychat.databinding.ActivitySearchUserBinding
import com.example.easychat.model.UserModel
import com.example.easychat.searchUser.adapterAndHolder.SearchUserAdapter
import com.example.easychat.utils.FirebaseUtil
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.Query

class SearchUserActivity : AppCompatActivity() {

    private lateinit var binding : ActivitySearchUserBinding

    private lateinit var adapter: SearchUserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initUI()
    }

    private fun initUI() {
        binding.etSearchUser.requestFocus()
        initListeners()
        initTextChanges()
    }

    private fun initTextChanges() {
        binding.etSearchUser.doAfterTextChanged {
            binding.etSearchUser.error = null
        }
    }

    private fun initListeners() {
        binding.ibtnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.iBtnSearch.setOnClickListener {
            val username = binding.etSearchUser.text.toString().trim()

            if (username.isNotEmpty() && username.length >= 5){
                setupSearchRecyclerView(username)
            }else{
                binding.etSearchUser.error = "Nombre invalido"
            }
        }
    }

    private fun setupSearchRecyclerView(username: String) {

        val query : Query = FirebaseUtil().allUserCollectionReference()
            .whereGreaterThanOrEqualTo("username", username)

        val options = FirestoreRecyclerOptions.Builder<UserModel>()
            .setQuery(query, UserModel::class.java).build()

        adapter = SearchUserAdapter(options)

        binding.rvSearchUsers.layoutManager = LinearLayoutManager(this)
        binding.rvSearchUsers.adapter = adapter
        adapter.startListening()
    }

}