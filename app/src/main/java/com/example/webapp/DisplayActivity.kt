package com.example.webapp

import android.annotation.SuppressLint
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class DisplayActivity : AppCompatActivity() {
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display)

        val textName = findViewById<TextView>(R.id.textName)
        val textEmail = findViewById<TextView>(R.id.textEmail)
        val imageView = findViewById<ImageView>(R.id.imageView)

        val name = intent.getStringExtra("name")
        val email = intent.getStringExtra("email")
        textName.text = "Name: $name"
        textEmail.text = "Email: $email"
        val imagePath = intent.getStringExtra("imagePath")
        if (!imagePath.isNullOrEmpty()) {
            val bitmap = BitmapFactory.decodeFile(imagePath)
            imageView.setImageBitmap(bitmap)
        }
    }
}