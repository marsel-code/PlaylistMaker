package com.example.playlistmaker.main.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker.R
import com.example.playlistmaker.media.ui.MediaActivity
import com.example.playlistmaker.search.ui.SearchActivity
import com.example.playlistmaker.settings.ui.SettingsActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        val searchButton = findViewById<Button>(R.id.search)
        searchButton.setOnClickListener {
            val searchButtonIntent = Intent(this, SearchActivity::class.java)
            startActivity(searchButtonIntent)
        }
        val mediaButton = findViewById<Button>(R.id.media)
        mediaButton.setOnClickListener {
            val mediaButtonIntent = Intent(this, MediaActivity::class.java)
            startActivity(mediaButtonIntent)
        }
        val settingButton = findViewById<Button>(R.id.setting)
        settingButton.setOnClickListener {
            val settingButtonIntent = Intent(this, SettingsActivity::class.java)
            startActivity(settingButtonIntent)
        }
    }
}