package com.guilhermenettizp.redesocial.auth

import com.google.firebase.auth.FirebaseAuth

class UserAuth {

    private val firebaseAuth = FirebaseAuth.getInstance()

    fun getCurrentUser() = firebaseAuth.currentUser

    fun getCurrentUserEmail(): String? {
        return firebaseAuth.currentUser?.email
    }

    fun login(email: String, password: String, callback: (Boolean, String?) -> Unit) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    callback(true, null)
                } else {
                    callback(false, task.exception?.message)
                }
            }
    }

    fun register(email: String, password: String, callback: (Boolean, String?) -> Unit) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    callback(true, null)
                } else {
                    callback(false, task.exception?.message)
                }
            }
    }

    fun logout() {
        firebaseAuth.signOut()
    }
}