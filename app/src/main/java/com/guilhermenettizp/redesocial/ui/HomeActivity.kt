package com.guilhermenettizp.redesocial.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.location.Address
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
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
import com.guilhermenettizp.redesocial.helper.LocalizacaoHelper
import com.guilhermenettizp.redesocial.model.Post

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    private val userAuth = UserAuth()
    private val userDAO = UserDAO()

    private var posts = ArrayList<Post>()
    private lateinit var adapter: PostAdapter

    private var imagemSelecionada: Bitmap? = null
    private var ultimoTimestamp: Timestamp? = null

    private val LOCATION_PERMISSION_CODE = 1001

    private val galeria = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        if (uri != null) {
            binding.imgPreviewPost.setImageURI(uri)
            binding.imgPreviewPost.visibility = View.VISIBLE

            val drawable = binding.imgPreviewPost.drawable
            val base64 = Base64Converter.drawableToString(drawable)
            imagemSelecionada = Base64Converter.stringToBitmap(base64)
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

            solicitarLocalizacaoEPostar(descricao)
        }

        binding.btnRefresh.setOnClickListener {
            carregarPosts(reset = true)
        }

        binding.imgProfile.setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }

        binding.btnBuscarCidade.setOnClickListener {

            val cidade = binding.edtBuscarCidade.text.toString()

            if (cidade.isEmpty()) {
                Toast.makeText(this, "Digite uma cidade", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            buscarPostsPorCidade(cidade)
        }
    }

    private fun solicitarLocalizacaoEPostar(descricao: String) {

        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_CODE
            )
            return
        }

        val helper = LocalizacaoHelper(this)

        helper.obterLocalizacaoAtual(object : LocalizacaoHelper.Callback {

            override fun onLocalizacaoRecebida(
                endereco: Address,
                latitude: Double,
                longitude: Double
            ) {

                val cidade = endereco.locality
                    ?: endereco.subAdminArea
                    ?: "Desconhecida"

                salvarPost(descricao, cidade)
            }

            override fun onErro(mensagem: String) {
                salvarPost(descricao, "Desconhecida")
            }
        })
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == LOCATION_PERMISSION_CODE &&
            grantResults.isNotEmpty() &&
            grantResults[0] == PackageManager.PERMISSION_GRANTED
        ) {
            Toast.makeText(this, "Permissão concedida", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Permissão negada", Toast.LENGTH_SHORT).show()
        }
    }

    private fun salvarPost(descricao: String, cidade: String) {

        val drawable = binding.imgPreviewPost.drawable
        val imageString = Base64Converter.drawableToString(drawable)

        val dados = hashMapOf(
            "descricao" to descricao,
            "imageString" to imageString,
            "cidade" to cidade,
            "data" to Timestamp.now()
        )

        Firebase.firestore.collection("posts")
            .add(dados)
            .addOnSuccessListener {

                Toast.makeText(this, "Post criado!", Toast.LENGTH_SHORT).show()

                binding.edtDescricaoPost.text.clear()
                binding.imgPreviewPost.setImageResource(0)
                binding.imgPreviewPost.visibility = View.GONE

                posts.clear()
                ultimoTimestamp = null
                carregarPosts()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Erro ao criar post", Toast.LENGTH_SHORT).show()
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

                for (doc in documentos.documents) {

                    val descricao = doc.getString("descricao") ?: ""
                    val imageString = doc.getString("imageString") ?: ""
                    val cidade = doc.getString("cidade") ?: "Desconhecida"

                    try {
                        val bitmap = Base64Converter.stringToBitmap(imageString)
                        posts.add(Post(descricao, bitmap, cidade))
                    } catch (e: Exception) {
                        Toast.makeText(this, "Erro em um post", Toast.LENGTH_SHORT).show()
                    }
                }

                adapter.notifyDataSetChanged()
            }
        }
    }

    private fun buscarPostsPorCidade(cidade: String) {

        posts.clear()

        Firebase.firestore.collection("posts")
            .whereEqualTo("cidade", cidade)
            .get()
            .addOnSuccessListener { documentos ->

                for (document in documentos) {

                    val descricao = document.getString("descricao") ?: ""
                    val imageString = document.getString("imageString") ?: ""
                    val cidadePost = document.getString("cidade") ?: "Desconhecida"

                    try {
                        val bitmap = Base64Converter.stringToBitmap(imageString)
                        posts.add(Post(descricao, bitmap, cidadePost))
                    } catch (e: Exception) {
                        Toast.makeText(this, "Erro em um post", Toast.LENGTH_SHORT).show()
                    }
                }

                adapter.notifyDataSetChanged()

                if (posts.isEmpty()) {
                    Toast.makeText(this, "Nenhum post encontrado", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { e ->
                e.printStackTrace()
                Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
            }
    }
}