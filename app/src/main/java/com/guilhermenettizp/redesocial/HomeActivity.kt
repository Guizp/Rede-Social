package com.guilhermenettizp.redesocial

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.guilhermenettizp.redesocial.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val firebaseAuth = FirebaseAuth.getInstance()
        val user = firebaseAuth.currentUser

        if (user == null) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        }

        val email = user.email ?: ""
        val db = Firebase.firestore

        db.collection("usuarios")
            .document(email)
            .get()
            .addOnCompleteListener { task ->

                if (!task.isSuccessful) {
                    Toast.makeText(this, "Erro ao carregar dados", Toast.LENGTH_LONG).show()
                    return@addOnCompleteListener
                }

                val document = task.result

                if (document == null || !document.exists()) {
                    startActivity(Intent(this, ProfileActivity::class.java))
                    finish()
                    return@addOnCompleteListener
                }

                val data = document.data

                val username = data?.get("username")?.toString() ?: "Sem username"
                val nomeCompleto = data?.get("nomeCompleto")?.toString() ?: "Sem nome"

                binding.txtUsername.text = username
                binding.txtNomeCompleto.text = nomeCompleto

                val imageString = data?.get("fotoPerfil")?.toString()

                if (!imageString.isNullOrEmpty()) {
                    try {
                        val bitmap = Base64Converter.stringToBitmap(imageString)
                        binding.imgLogo.setImageBitmap(bitmap)
                    } catch (e: Exception) {
                        Toast.makeText(this, "Erro ao carregar imagem", Toast.LENGTH_SHORT).show()
                    }
                }
            }

        binding.btnSair.setOnClickListener {
            firebaseAuth.signOut()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
}