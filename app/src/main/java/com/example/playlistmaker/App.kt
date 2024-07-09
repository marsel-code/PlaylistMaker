package com.example.playlistmaker

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.creator.Creator

const val APP_SHARED_PREFERENCES = "app_shared_preferences"
const val THEME_SHARED_PREFERENCES_KEY = "key_for_dark_theme"

class App : Application() {


    override fun onCreate() {
        super.onCreate()
        val sharedPrefs = getSharedPreferences(APP_SHARED_PREFERENCES, MODE_PRIVATE)
        switchTheme(sharedPrefs.getBoolean(THEME_SHARED_PREFERENCES_KEY, false))
        Creator.initApplication(this)

    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO

            }
        )
    }

}