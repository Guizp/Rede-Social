package com.guilhermenettizp.redesocial.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.guilhermenettizp.redesocial.auth.UserAuth
import com.guilhermenettizp.redesocial.dao.UserDAO
import com.guilhermenettizp.redesocial.databinding.ActivityProfileBinding
import com.guilhermenettizp.redesocial.model.User
import com.guilhermenettizp.redesocial.converter.Base64Converter

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding
    private val userAuth = UserAuth()
    private val userDAO = UserDAO()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val user = userAuth.getCurrentUser()

        if (user == null) {
            finish()
            return
        }

        val galeria = registerForActivityResult(
            ActivityResultContracts.PickVisualMedia()
        ) { uri ->
            if (uri != null) {
                binding.profilePicture.setImageURI(uri)
            } else {
                Toast.makeText(this, "Nenhuma imagem selecionada", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnAlterarFoto.setOnClickListener {
            galeria.launch(
                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
            )
        }

        binding.btnSalvar.setOnClickListener {

            val username = binding.etUsername.text.toString()
            val nomeCompleto = binding.etNomeCompleto.text.toString()

            if (username.isEmpty() || nomeCompleto.isEmpty()) {
                Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val foto = Base64Converter.drawableToString(binding.profilePicture.drawable)

            val userModel = User(
                email = user.email ?: "",
                username = username,
                nomeCompleto = nomeCompleto,
                fotoPerfil = foto
            )

            userDAO.salvarUsuario(userModel) {

                startActivity(Intent(this, HomeActivity::class.java))
                finish()
            }
        }
    }
}