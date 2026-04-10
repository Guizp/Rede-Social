package com.guilhermenettizp.redesocial.dao

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.guilhermenettizp.redesocial.model.User

class UserDAO {

    private val db = Firebase.firestore
    private val collection = "usuarios"

    fun salvarUsuario(user: User, callback: (Boolean) -> Unit) {
        db.collection(collection)
            .document(user.email)
            .set(user)
            .addOnSuccessListener {
                callback(true)
            }
            .addOnFailureListener {
                callback(false)
            }
    }

    fun buscarUsuario(email: String, callback: (User?) -> Unit) {
        db.collection(collection)
            .document(email)
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val user = document.toObject(User::class.java)
                    callback(user)
                } else {
                    callback(null)
                }
            }
            .addOnFailureListener {
                callback(null)
            }
    }
}