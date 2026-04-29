package com.guilhermenettizp.redesocial.ui

import android.app.Activity
import android.graphics.Bitmap
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.ImageRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.guilhermenettizp.redesocial.databinding.ActivityMainBinding

class MainActivity : Activity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val queue: RequestQueue = Volley.newRequestQueue(this)

        val urlTexto = "http://10.105.68.80:8080/exemplo/texto"
        val urlImagem = "http://10.105.68.80:8080/exemplo/imagem"

        val stringRequest = StringRequest(
            Request.Method.GET, urlTexto,
            { response ->
                binding.txtResposta.text = response
            },
            { error ->
                error.printStackTrace()
                Toast.makeText(this, "Erro ao buscar texto", Toast.LENGTH_SHORT).show()
            }
        )

        val imageRequest = ImageRequest(
            urlImagem,
            { response: Bitmap ->
                binding.imagePost.setImageBitmap(response)
            },
            0, 0,
            ImageView.ScaleType.CENTER_CROP,
            Bitmap.Config.RGB_565,
            { error ->
                error.printStackTrace()
                Toast.makeText(this, "Erro ao carregar imagem", Toast.LENGTH_SHORT).show()
            }
        )

        queue.add(stringRequest)
        queue.add(imageRequest)
    }
}