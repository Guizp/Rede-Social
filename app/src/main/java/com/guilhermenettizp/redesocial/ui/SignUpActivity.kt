package com.guilhermenettizp.redesocial.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.guilhermenettizp.redesocial.auth.UserAuth
import com.guilhermenettizp.redesocial.databinding.ActivitySignUpBinding

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding
    private val userAuth = UserAuth()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnCriarConta.setOnClickListener {

            val email = binding.edtEmail.text.toString()
            val senha = binding.edtSenha.text.toString()
            val confirmar = binding.edtConfirmarSenha.text.toString()

            if (email.isEmpty() || senha.isEmpty() || confirmar.isEmpty()) {
                Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (senha != confirmar) {
                Toast.makeText(this, "Senhas diferentes", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            userAuth.register(email, senha) { sucesso, erro ->

                if (sucesso) {
                    startActivity(Intent(this, ProfileActivity::class.java))
                    finish()
                } else {
                    Toast.makeText(this, erro ?: "Erro ao cadastrar", Toast.LENGTH_LONG).show()
                }
            }
        }

        binding.btnVoltar.setOnClickListener {
            finish()
        }
    }
}