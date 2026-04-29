package com.guilhermenettizp.redesocial.ui

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.Timestamp
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.guilhermenettizp.redesocial.adapter.PostAdapter
import com.guilhermenettizp.redesocial.auth.UserAuth
import com.guilhermenettizp.redesocial.converter.Base64Converter
import com.guilhermenettizp.redesocial.dao.UserDAO
import com.guilhermenettizp.redesocial.databinding.ActivityHomeBinding
import com.guilhermenettizp.redesocial.model.Post

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    private val userAuth = UserAuth()
    private val userDAO = UserDAO()

    private var posts = ArrayList<Post>()
    private lateinit var adapter: PostAdapter

    private var imagemSelecionada: Bitmap? = null

    private var ultimoTimestamp: Timestamp? = null

    private val galeria = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        if (uri != null) {
            binding.imgPreviewPost.setImageURI(uri)
            binding.imgPreviewPost.visibility = android.view.View.VISIBLE

            val drawable = binding.imgPreviewPost.drawable
            val bitmap = Base64Converter.drawableToString(drawable)
            imagemSelecionada = Base64Converter.stringToBitmap(bitmap)

        } else {
            Toast.makeText(this, "Nenhuma imagem selecionada", Toast.LENGTH_SHORT).show()
        }
    }

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
                    binding.imgProfile.setImageBitmap(bitmap)
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

        adapter = PostAdapter(posts)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter

        carregarPosts()

        binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (!recyclerView.canScrollVertically(1)) {
                    carregarPosts()
                }
            }
        })

        binding.btnSelecionarImagem.setOnClickListener {
            galeria.launch("image/*")
        }

        binding.btnAddPost.setOnClickListener {

            val descricao = binding.edtDescricaoPost.text.toString()

            if (descricao.isEmpty() || imagemSelecionada == null) {
                Toast.makeText(this, "Preencha tudo", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val drawable = binding.imgPreviewPost.drawable
            val imageString = Base64Converter.drawableToString(drawable)

            val dados = hashMapOf(
                "descricao" to descricao,
                "imageString" to imageString,
                "data" to Timestamp.now() //
            )

            Firebase.firestore.collection("posts")
                .add(dados)
                .addOnSuccessListener {

                    Toast.makeText(this, "Post criado!", Toast.LENGTH_SHORT).show()

                    binding.edtDescricaoPost.text.clear()
                    binding.imgPreviewPost.setImageResource(0)
                    binding.imgPreviewPost.visibility = android.view.View.GONE

                    posts.clear()
                    ultimoTimestamp = null
                    carregarPosts()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Erro ao criar post", Toast.LENGTH_SHORT).show()
                }
        }

        binding.btnRefresh.setOnClickListener {
            carregarPosts(reset = true)
        }

        binding.imgProfile.setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }
    }

    private fun carregarPosts(reset: Boolean = false) {

        if (reset) {
            posts.clear()
            ultimoTimestamp = null
        }

        var query = Firebase.firestore
            .collection("posts")
            .orderBy("data", Query.Direction.DESCENDING)
            .limit(5)

        if (ultimoTimestamp != null) {
            query = query.startAfter(ultimoTimestamp!!)
        }

        query.get().addOnSuccessListener { documentos ->

            if (!documentos.isEmpty) {

                ultimoTimestamp = documentos.documents.last().getTimestamp("data")

                for (document in documentos.documents) {

                    val imageString = document.getString("imageString") ?: ""
                    val descricao = document.getString("descricao") ?: ""

                    try {
                        val bitmap = Base64Converter.stringToBitmap(imageString)

                        val post = Post(descricao, bitmap)
                        if (!posts.contains(post)) {
                            posts.add(post)
                        }

                    } catch (e: Exception) {
                        Toast.makeText(this, "Erro em um post", Toast.LENGTH_SHORT).show()
                    }
                }

                adapter.notifyDataSetChanged()
            }
        }
    }
}