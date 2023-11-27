package com.example.easychat.chat.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.AdapterDataObserver
import com.example.easychat.chat.adapter.ChatAdapter
import com.example.easychat.chat.model.ChatMessageModel
import com.example.easychat.chat.model.ChatroomModel
import com.example.easychat.databinding.ActivityChatBinding
import com.example.easychat.model.UserModel
import com.example.easychat.searchUser.adapterAndHolder.SearchUserAdapter
import com.example.easychat.utils.AndroidUtil
import com.example.easychat.utils.FirebaseUtil
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.Timestamp
import com.google.firebase.firestore.Query

class ChatActivity : AppCompatActivity() {

    private lateinit var binding : ActivityChatBinding

    private lateinit var mOtherUser : UserModel
    private lateinit var mChatroomId : String

    private var mChatroomModel : ChatroomModel? = null

    private lateinit var mAdapter : ChatAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initUI()

    }

    private fun initUI() {
        getInfo()
        setUserInfo()
        getOrCreateChatroomModel()
        setUpChatMessages()
        initListeners()
    }

    private fun setUpChatMessages() {

        val query : Query = FirebaseUtil().getChatroomMessageReference(mChatroomId)
            .orderBy("lastMessageTimestamp", Query.Direction.ASCENDING)

        val options = FirestoreRecyclerOptions.Builder<ChatMessageModel>()
            .setQuery(query, ChatMessageModel::class.java).build()

        mAdapter = ChatAdapter(options)
        binding.rvMessages.layoutManager = LinearLayoutManager(this)
        binding.rvMessages.adapter = mAdapter
        mAdapter.startListening()

        mAdapter.registerAdapterDataObserver(object : AdapterDataObserver(){
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                super.onItemRangeInserted(positionStart, itemCount)
                binding.rvMessages.smoothScrollToPosition(0)
            }
        })
    }

    private fun getOrCreateChatroomModel() {
        FirebaseUtil().getChatroomReference(mChatroomId).get().addOnCompleteListener {
            if (it.isSuccessful){
                mChatroomModel = it.result.toObject(ChatroomModel::class.java)

                if (mChatroomModel==null){
                    mChatroomModel = ChatroomModel(
                        mChatroomId,
                        listOf(FirebaseUtil().currentUserId(),mOtherUser.userId),
                        Timestamp.now(),
                        ""
                    )
                    FirebaseUtil().getChatroomReference(mChatroomId).set(mChatroomModel!!)
                }

            }
        }
    }

    private fun getInfo() {
        mOtherUser = AndroidUtil().getUSer(intent)
        mChatroomId = FirebaseUtil().getChatroomId(FirebaseUtil().currentUserId(), mOtherUser.userId)
    }

    private fun setUserInfo() {
        binding.tvTitleUsername.text = mOtherUser.username
    }

    private fun initListeners() {
        binding.ibtnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.iBtnSendMessage.setOnClickListener {
            val message = binding.etMessage.text.toString().trim()

            if (message.isEmpty()){
                return@setOnClickListener
            }

            sendMessage(message)
        }
    }

    private fun sendMessage(message: String) {

        mChatroomModel?.lastMessageTimestamp = Timestamp.now()
        mChatroomModel?.lastMessageSenderId = FirebaseUtil().currentUserId()
        mChatroomModel?.lastMessage = message
        FirebaseUtil().getChatroomReference(mChatroomId).set(mChatroomModel!!)

        val chatMessageModel = ChatMessageModel(
            message,
            FirebaseUtil().currentUserId(),
            Timestamp.now()
        )

        FirebaseUtil().getChatroomMessageReference(mChatroomId).add(chatMessageModel)
            .addOnCompleteListener {
            if (it.isSuccessful){
                binding.etMessage.setText("")
            }
        }

    }
}