package com.example.playlistmaker.ui.tracks

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker.APP_SHARED_PREFERENCES
import com.example.playlistmaker.App
import com.example.playlistmaker.R
import com.example.playlistmaker.THEME_SHARED_PREFERENCES_KEY
import com.google.android.material.switchmaterial.SwitchMaterial

class SettingsActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.settings_activity)

        val backButton = findViewById<androidx.appcompat.widget.Toolbar>(R.id.backMain)
        backButton.setOnClickListener {
            finish()
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

        val themeSwitcher = findViewById<SwitchMaterial>(R.id.themeSwitcher)

        val sharedPrefs = getSharedPreferences(APP_SHARED_PREFERENCES, MODE_PRIVATE)

        themeSwitcher.isChecked = sharedPrefs.getBoolean(THEME_SHARED_PREFERENCES_KEY, false)

        themeSwitcher.setOnCheckedChangeListener { switcher, checked ->
            (applicationContext as App).switchTheme(checked)
            sharedPrefs.edit()
                .putBoolean(THEME_SHARED_PREFERENCES_KEY, checked)
                .apply()
        }
    }
}