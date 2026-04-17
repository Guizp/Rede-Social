package com.guilhermenettizp.redesocial.ui

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.guilhermenettizp.redesocial.auth.UserAuth
import com.guilhermenettizp.redesocial.converter.Base64Converter
import com.guilhermenettizp.redesocial.dao.UserDAO
import com.guilhermenettizp.redesocial.databinding.ActivityProfileBinding
import com.guilhermenettizp.redesocial.model.User

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding

    private val userAuth = UserAuth()
    private val userDAO = UserDAO()

    private var imagemSelecionada: Bitmap? = null

    private val galeria = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        if (uri != null) {
            binding.profilePicture.setImageURI(uri)

            val drawable = binding.profilePicture.drawable
            val base64 = Base64Converter.drawableToString(drawable)
            imagemSelecionada = Base64Converter.stringToBitmap(base64)

        } else {
            Toast.makeText(this, "Nenhuma imagem selecionada", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val user = userAuth.getCurrentUser()

        if (user == null) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        }

        val email = user.email ?: ""

        userDAO.buscarUsuario(email) { userModel ->

            if (userModel != null) {

                binding.etUsername.setText(userModel.username)
                binding.etNomeCompleto.setText(userModel.nomeCompleto)

                binding.etNomeCompleto.isEnabled = false

                if (userModel.fotoPerfil.isNotEmpty()) {
                    try {
                        val bitmap = Base64Converter.stringToBitmap(userModel.fotoPerfil)
                        binding.profilePicture.setImageBitmap(bitmap)
                    } catch (e: Exception) {
                        Toast.makeText(this, "Erro ao carregar imagem", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        binding.btnAlterarFoto.setOnClickListener {
            galeria.launch("image/*")
        }

        binding.btnSalvar.setOnClickListener {

            val username = binding.etUsername.text.toString()
            val nomeCompleto = binding.etNomeCompleto.text.toString()

            if (username.isEmpty()) {
                Toast.makeText(this, "Preencha o username", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val drawable = binding.profilePicture.drawable
            val fotoString = Base64Converter.drawableToString(drawable)

            val userModel = User(
                email = email,
                username = username,
                nomeCompleto = nomeCompleto,
                fotoPerfil = fotoString
            )

            userDAO.salvarUsuario(userModel) { sucesso ->

                if (sucesso) {
                    Toast.makeText(this, "Perfil salvo!", Toast.LENGTH_SHORT).show()

                    startActivity(Intent(this, HomeActivity::class.java))
                    finish()
                } else {
                    Toast.makeText(this, "Erro ao salvar perfil", Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.btnVoltar.setOnClickListener {
            finish()
        }
    }
}