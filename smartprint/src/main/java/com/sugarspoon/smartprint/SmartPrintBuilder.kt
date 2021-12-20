package com.sugarspoon.smartprint

import android.Manifest
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class SmartPrintBuilder(builder: Builder) {

    private val view: View = builder.view
    private val context: Context = builder.context

    class Builder(val context: Context) {

        lateinit var view: View

        fun setViewForScreenShoot(view: View): Builder {
            this.view = view
            return this
        }

        fun requestPermission(): Builder {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                (context as AppCompatActivity).requestPermissions(
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    100
                )
            }
            return this
        }

        fun build(): SmartPrintBuilder {
            return SmartPrintBuilder(this)
        }

    }

    private fun takeScreenShoot(): Bitmap {
        val bitmap = Bitmap.createBitmap(
            view.width,
            view.height,
            Bitmap.Config.ARGB_8888
        )
        view.draw(Canvas(bitmap))
        return bitmap
    }

    private fun checkPermission() =
        ContextCompat.checkSelfPermission(context, WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED

    fun takeAndShareScreenShoot() {
        if (checkPermission()) {
            val icon: Bitmap = takeScreenShoot()
            val share = Intent(Intent.ACTION_SEND)
            share.type = "image/jpeg"
            val values = ContentValues()
            values.put(MediaStore.Images.Media.TITLE, "title")
            values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")

            try {
                val uri: Uri? = context.contentResolver.insert(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    values
                )
                val upstream = uri?.let { context.contentResolver.openOutputStream(it) }
                icon.compress(Bitmap.CompressFormat.JPEG, 100, upstream)
                upstream?.close()
                share.putExtra(Intent.EXTRA_STREAM, uri)
                context.startActivity(Intent.createChooser(share, "Share Image"))
            } catch (e: Exception) {
                System.err.println(e.toString())
                context.apply {
                    Toast.makeText(this, SHARE_ERROR, Toast.LENGTH_LONG).show()
                }
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                (context as AppCompatActivity).requestPermissions(
                    arrayOf(WRITE_EXTERNAL_STORAGE),
                    REQUEST_CODE
                )
            }
        }
    }

    companion object {
        private const val SHARE_ERROR = "Falha inesperada"
        private const val REQUEST_CODE = 100
    }
}