package com.guilhermenettizp.redesocial.converter

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.Base64
import java.io.ByteArrayOutputStream

class Base64Converter {
    companion object {

        fun drawableToString(drawable: Drawable): String {
            val bitmap = (drawable as BitmapDrawable).bitmap
            val resized = Bitmap.createScaledBitmap(bitmap, 150, 150, true)

            val outputStream = ByteArrayOutputStream()
            resized.compress(Bitmap.CompressFormat.PNG, 100, outputStream)

            return Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT)
        }

        fun stringToBitmap(imageString: String): Bitmap {
            val bytes = Base64.decode(imageString, Base64.DEFAULT)
            return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
        }
    }
}