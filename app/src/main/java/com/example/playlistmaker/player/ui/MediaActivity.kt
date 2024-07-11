package com.example.playlistmaker.player.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker.R

class MediaActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_media)
        val backButton = findViewById<androidx.appcompat.widget.Toolbar>(R.id.backMain)
        backButton.setOnClickListener {
          finish()
        }
    }
}