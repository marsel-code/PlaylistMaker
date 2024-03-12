package com.example.playlistmaker

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    //    @SuppressLint("MissingInflatedId", "WrongViewCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
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

        //           val media = findViewById<Button>(R.id.media)
//        media.setOnClickListener {
//            Toast.makeText(this@MainActivity, "Нажали на Медиатека!", Toast.LENGTH_SHORT).show()
//        }
    }
}