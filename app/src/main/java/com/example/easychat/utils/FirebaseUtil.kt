package com.example.easychat.utils

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

class FirebaseUtil {

     private fun currentUserId(): String? {
        return FirebaseAuth.getInstance().uid
    }

    fun isLoggedIn():Boolean{
        if (currentUserId() != null){
            return true
        }
        return false
    }

    fun currentUserDetails():DocumentReference{
        return FirebaseFirestore.getInstance().collection("users").document(currentUserId()!!)
    }
}