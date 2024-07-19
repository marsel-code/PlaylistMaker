package com.example.playlistmaker.settings.presentation.ui


import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker.App
import com.example.playlistmaker.R
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.settings.domain.model.ThemeSettings
import com.google.android.material.switchmaterial.SwitchMaterial

class SettingsActivity : AppCompatActivity() {

    private val getSettingsInteractor = Creator.provideSettingsInteractor()
    private val getSharingInteractor = Creator.provideSharingInteractor()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.settings_activity)

        val backButton = findViewById<androidx.appcompat.widget.Toolbar>(R.id.backMain)
        backButton.setOnClickListener {
            finish()
        }

        val shareButton = findViewById<TextView>(R.id.share)
        shareButton.setOnClickListener {
            getSharingInteractor.openTerms()
//            val shareIntent = Intent(Intent.ACTION_SEND)
//            shareIntent.setType("text/playn")
//            shareIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.shareButton))
//            startActivity(shareIntent)
        }

        val supportButton = findViewById<TextView>(R.id.support)
        supportButton.setOnClickListener {
            getSharingInteractor.openSupport()
//            val shareIntent = Intent(Intent.ACTION_SENDTO)
//            shareIntent.data = Uri.parse("mailto:")
//            shareIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.supportMail)))
//            shareIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.supportSubject))
//            shareIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.supportMessage))
//            startActivity(shareIntent)
        }

        val agreementUserButton = findViewById<TextView>(R.id.agreementUser)
        agreementUserButton.setOnClickListener {
            getSharingInteractor.shareApp()

//            val shareIntent = Intent(Intent.ACTION_VIEW)
//            shareIntent.data = Uri.parse(getString(R.string.agreementUser))
//            startActivity(shareIntent)
        }

        val themeSwitcher = findViewById<SwitchMaterial>(R.id.themeSwitcher)

        themeSwitcher.isChecked = getSettingsInteractor.getThemeSettings().themeState


        themeSwitcher.setOnCheckedChangeListener { switcher, checked ->
            (applicationContext as App).switchTheme(checked)
            getSettingsInteractor.updateThemeSetting(ThemeSettings(checked))
        }

//        val sharedPrefs = getSharedPreferences(APP_SHARED_PREFERENCES, MODE_PRIVATE)
//
//        themeSwitcher.isChecked = sharedPrefs.getBoolean(THEME_SHARED_PREFERENCES_KEY, false)
//
//        themeSwitcher.setOnCheckedChangeListener { switcher, checked ->
//            (applicationContext as App).switchTheme(checked)
//            sharedPrefs.edit()
//                .putBoolean(THEME_SHARED_PREFERENCES_KEY, checked)
//                .apply()
//        }


    }
}