package com.guilhermenettizp.redesocial.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.guilhermenettizp.redesocial.auth.UserAuth
import com.guilhermenettizp.redesocial.dao.UserDAO
import com.guilhermenettizp.redesocial.databinding.ActivityHomeBinding
import com.guilhermenettizp.redesocial.converter.Base64Converter

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private val userAuth = UserAuth()
    private val userDAO = UserDAO()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val user = userAuth.getCurrentUser()

        if (user == null) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        }

        val email = user.email ?: ""

        userDAO.buscarUsuario(email) { userModel ->

            if (userModel == null) {
                startActivity(Intent(this, ProfileActivity::class.java))
                finish()
                return@buscarUsuario
            }

            binding.txtUsername.text = userModel.username
            binding.txtNomeCompleto.text = userModel.nomeCompleto

            if (userModel.fotoPerfil.isNotEmpty()) {
                try {
                    val bitmap = Base64Converter.stringToBitmap(userModel.fotoPerfil)
                    binding.imgLogo.setImageBitmap(bitmap)
                } catch (e: Exception) {
                    Toast.makeText(this, "Erro ao carregar imagem", Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.btnSair.setOnClickListener {
            userAuth.logout()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
}