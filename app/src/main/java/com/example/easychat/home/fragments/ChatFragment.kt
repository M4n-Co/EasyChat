package com.example.easychat.home.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.easychat.chat.model.ChatroomModel
import com.example.easychat.databinding.FragmentChatBinding
import com.example.easychat.home.adapter.RecentChatsAdapter
import com.example.easychat.utils.FirebaseUtil
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.Query

class ChatFragment : Fragment() {

    private var _binding : FragmentChatBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: RecentChatsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChatBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initUI()

    }

    private fun initUI() {
        initRecycler()
    }

    private fun initRecycler() {

        val query = FirebaseUtil().allChatroomCollectionReference()
            .whereArrayContains("userIds", FirebaseUtil().currentUserId()!!)
            .orderBy("lastMessageTimestamp",Query.Direction.DESCENDING)

        val options = FirestoreRecyclerOptions.Builder<ChatroomModel>()
            .setQuery(query, ChatroomModel::class.java).build()

        adapter = RecentChatsAdapter(options)

        binding.rvChats.layoutManager = LinearLayoutManager(requireContext())
        binding.rvChats.adapter = adapter

        adapter.startListening()

    }

    override fun onResume() {
        super.onResume()
        adapter.notifyDataSetChanged()
    }

}