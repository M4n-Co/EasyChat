package com.example.easychat.utils

import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Locale

class FirebaseUtil {
    companion object{
        const val USERS_COLLECTIONS = "users"
        const val CHATROOM_COLLECTIONS = "chatrooms"
        const val CHATS = "chats"
    }

    fun currentUserId(): String? {
        return FirebaseAuth.getInstance().uid
    }

    fun isLoggedIn():Boolean{
        if (currentUserId() != null){
            return true
        }
        return false
    }

    fun currentUserDetails():DocumentReference{
        return FirebaseFirestore.getInstance().collection(USERS_COLLECTIONS).document(currentUserId()!!)
    }

    fun allUserCollectionReference() : CollectionReference{
        return FirebaseFirestore.getInstance().collection(USERS_COLLECTIONS)
    }

    fun getChatroomMessageReference(chatroomId: String) : CollectionReference{
        return getChatroomReference(chatroomId).collection(CHATS)
    }

    fun getChatroomReference(chatroomId:String):DocumentReference{
        return FirebaseFirestore.getInstance().collection(CHATROOM_COLLECTIONS).document(chatroomId)
    }

    fun getChatroomId(userOne : String?, userTwo : String?):String{
        return if (userOne.hashCode()<userTwo.hashCode()){
            userOne+"_"+userTwo
        }else{
            userTwo+"_"+userOne
        }
    }

    fun allChatroomCollectionReference():CollectionReference{
        return FirebaseFirestore.getInstance().collection(CHATROOM_COLLECTIONS)
    }

    fun getOtherUserFromChatroom(userIds: List<String?>) : DocumentReference{
        return if (userIds[0] == currentUserId()){
            allUserCollectionReference().document(userIds[1]!!)
        }else{
            allUserCollectionReference().document(userIds[0]!!)
        }
    }

    fun getTime(timestamp: Timestamp):String{
        return SimpleDateFormat("hh:mm", Locale.US).format(timestamp.toDate())
    }

    fun logout(){
        FirebaseAuth.getInstance().signOut()
    }
}