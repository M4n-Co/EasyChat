package com.example.easychat.chat.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.AdapterDataObserver
import com.example.easychat.chat.adapter.ChatAdapter
import com.example.easychat.chat.model.ChatMessageModel
import com.example.easychat.chat.model.ChatroomModel
import com.example.easychat.databinding.ActivityChatBinding
import com.example.easychat.model.UserModel
import com.example.easychat.utils.AndroidUtil
import com.example.easychat.utils.FirebaseUtil
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.Timestamp
import com.google.firebase.firestore.Query
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException

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
            .orderBy("lastMessageTimestamp", Query.Direction.DESCENDING)

        val options = FirestoreRecyclerOptions.Builder<ChatMessageModel>()
            .setQuery(query, ChatMessageModel::class.java).build()

        mAdapter = ChatAdapter(options)

        val linearLayoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.VERTICAL,
            true)

        binding.rvMessages.layoutManager = linearLayoutManager
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
                sendNotification(message)
            }
        }

    }

    private fun sendNotification(message: String) {

        FirebaseUtil().currentUserDetails().get().addOnCompleteListener {
            if (it.isSuccessful){
                val currentUser = it.result.toObject(UserModel::class.java)!!

                try {
                    val jsonObject = JSONObject()

                    val notificationJSONObject = JSONObject()
                    notificationJSONObject.put("title", currentUser.username)
                    notificationJSONObject.put("body",message)

                    val dataJSONObject = JSONObject()
                    dataJSONObject.put("userId", currentUser.userId)

                    jsonObject.put("notification", notificationJSONObject)
                    jsonObject.put("data", dataJSONObject)
                    jsonObject.put("to", mOtherUser.fcmToken)

                    callApi(jsonObject)

                }catch (_:Exception){

                }
            }
        }

    }

    private fun callApi(jsonObject: JSONObject){
        val json = "application/json; charset=utf-8".toMediaType()
        val client = OkHttpClient()

        val url = "https://fcm.googleapis.com/fcm/send"

        val body = jsonObject.toString().toRequestBody(json)

        val request = Request.Builder()
            .url(url)
            .patch(body)
            .header("Authorization","Bearer AAAAYCcYYUs:APA91bGeMi9pGsdgHrKVgonxQjZVZAfzg4-o6xdUqOUYtTPaQ01oEaubreOt8FaUYpSuooSmWs-Hj42BdK_XrxGPmoGfKXx0d-idFdxmeF7TbQNJTKFY7kXSljWVN0E8HDpyu9mA_qQQ")
            .build()

        client.newCall(request).enqueue(object : Callback{
            override fun onFailure(call: Call, e: IOException) {
            }

            override fun onResponse(call: Call, response: Response) {
            }

        })
    }


}