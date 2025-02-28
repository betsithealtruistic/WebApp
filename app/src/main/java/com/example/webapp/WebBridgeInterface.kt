package com.example.webapp


import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.provider.MediaStore
import android.util.Base64
import android.webkit.JavascriptInterface
import android.widget.ImageView
import android.widget.TextView
import java.io.ByteArrayOutputStream

class WebBridgeInterface(
    private val activity: Activity,
    private val webViewCallback: (String) -> Unit,
    private val nameTextView: TextView,
    private val introTextView: TextView,
    private val imageView: ImageView
) {

    private val CAMERA_REQUEST_CODE = 1001

    @JavascriptInterface
    fun openCamera() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        activity.startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE)
    }

    @JavascriptInterface
    fun returnData(name: String, intro: String, base64Image: String) {
        activity.runOnUiThread {
            nameTextView.text = "Name: $name"
            introTextView.text = "Intro: $intro"

            val imageBytes = Base64.decode(base64Image.split(",")[1], Base64.DEFAULT)
            val bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
            imageView.setImageBitmap(bitmap)
        }
    }

    fun handleCameraResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == CAMERA_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val bitmap = data?.extras?.get("data") as? Bitmap ?: return
            val base64Image = convertBitmapToBase64(bitmap)
            webViewCallback(base64Image)
        }
    }

    private fun convertBitmapToBase64(bitmap: Bitmap): String {
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        val byteArray = outputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }
}
