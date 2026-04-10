package com.guilhermenettizp.redesocial

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.guilhermenettizp.redesocial.databinding.ActivityProfileBinding

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding
    private var imageUri: Uri? = null

    private val galeria = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            imageUri = uri
            binding.profilePicture.setImageURI(uri)
        } else {
            Toast.makeText(this, "Nenhuma foto selecionada", Toast.LENGTH_LONG).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val firebaseAuth = FirebaseAuth.getInstance()

        val user = firebaseAuth.currentUser
        if (user == null) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        }

        binding.btnAlterarFoto.setOnClickListener {
            galeria.launch(
                PickVisualMediaRequest(
                    ActivityResultContracts.PickVisualMedia.ImageOnly
                )
            )
        }

        binding.btnSalvar.setOnClickListener {

            val email = user.email.toString()
            val username = binding.etUsername.text.toString()
            val nomeCompleto = binding.etNomeCompleto.text.toString()

            if (username.isEmpty() || nomeCompleto.isEmpty()) {
                Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            val drawable = binding.profilePicture.drawable
            if (drawable == null) {
                Toast.makeText(this, "Selecione uma imagem", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            val fotoPerfilString = Base64Converter.drawableToString(drawable)

            val db = Firebase.firestore

            val dados = hashMapOf(
                "nomeCompleto" to nomeCompleto,
                "username" to username,
                "fotoPerfil" to fotoPerfilString
            )
            
            db.collection("usuarios")
                .document(email)
                .set(dados)
                .addOnSuccessListener {
                    Toast.makeText(this, "Perfil salvo!", Toast.LENGTH_SHORT).show()

                    startActivity(Intent(this, HomeActivity::class.java))
                    finish()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Erro ao salvar", Toast.LENGTH_LONG).show()
                }
        }
    }
}