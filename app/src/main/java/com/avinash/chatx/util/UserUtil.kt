package com.avinash.chatx.util

import com.avinash.chatx.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

object UserUtil {
    var user: User? = null

    fun getCurrentUser() {
        if (FirebaseAuth.getInstance().currentUser != null) {
            FirebaseFirestore.getInstance().collection("Users")
                .document(FirebaseAuth.getInstance().currentUser?.uid!!)
                .get().addOnCompleteListener {
                    user = it.result?.toObject(User::class.java)

                }
        }
    }
}