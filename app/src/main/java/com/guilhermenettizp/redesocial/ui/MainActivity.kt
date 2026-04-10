package com.guilhermenettizp.redesocial.ui
import android.app.Activity
import android.graphics.Bitmap
import android.os.Bundle
import android.widget.ImageView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.ImageRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.guilhermenettizp.redesocial.databinding.ActivityMainBinding

class MainActivity : Activity()
{
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val queue: RequestQueue = Volley.newRequestQueue(this)
        val urlTexto = "http://10.105.68.80:8080/exemplo/texto"
        val urlImage = "http://10.105.68.80:8080/exemplo/imagem"

        val stringRequest = StringRequest(Request.Method.GET, urlTexto,
            { response ->
                binding.txtPost.text = response
            },
            { error ->
                error.printStackTrace()
            }
        )

        val imageRequest = ImageRequest(urlImage,
            { response ->
                binding.imagePost.setImageBitmap(response)
            },
            0, 0, ImageView.ScaleType.CENTER_CROP, Bitmap.Config.RGB_565,
            { error ->
                error.printStackTrace()
            }
        )
        queue.add(stringRequest)
        queue.add(imageRequest)
        queue.start()
    }
}