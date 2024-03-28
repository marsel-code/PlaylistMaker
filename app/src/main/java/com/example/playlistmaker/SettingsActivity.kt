package com.example.playlistmaker

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.TextView
import android.widget.Toolbar
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.settings_activity)

        val backButton = findViewById<androidx.appcompat.widget.Toolbar>(R.id.backMain)
        backButton.setOnClickListener {
            val backButtonIntent = Intent(this, MainActivity::class.java)
            startActivity(backButtonIntent)
        }

        val shareButton = findViewById<TextView>(R.id.share)
        shareButton.setOnClickListener {
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.setType("text/playn")
            shareIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.shareButton))
            startActivity(shareIntent)
        }

        val supportButton = findViewById<TextView>(R.id.support)
        supportButton.setOnClickListener {
            val shareIntent = Intent(Intent.ACTION_SENDTO)
            shareIntent.data = Uri.parse("mailto:")
            shareIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.supportMail)))
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.supportSubject))
            shareIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.supportMessage))
            startActivity(shareIntent)
        }

        val agreementUserButton = findViewById<TextView>(R.id.agreementUser)
        agreementUserButton.setOnClickListener {
            val shareIntent = Intent(Intent.ACTION_VIEW)
            shareIntent.data = Uri.parse(getString(R.string.agreementUser))
            startActivity(shareIntent)
        }


    }
}