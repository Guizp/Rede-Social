package com.guilhermenettizp.redesocial.model

import android.graphics.Bitmap
import com.google.firebase.Timestamp

data class Post(
    val descricao: String = "",
    val imagem: Bitmap? = null,
    val cidade: String = "",
    val data: Timestamp = Timestamp.now()
)