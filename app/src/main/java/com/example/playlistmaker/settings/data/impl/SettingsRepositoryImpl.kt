package com.example.playlistmaker.settings.data.impl

import android.content.SharedPreferences
import com.example.playlistmaker.APP_SHARED_PREFERENCES
import com.example.playlistmaker.THEME_SHARED_PREFERENCES_KEY

import com.example.playlistmaker.settings.domain.SettingsRepository
import com.example.playlistmaker.settings.domain.model.ThemeSettings

class SettingsRepositoryImpl(private val sharedPrefs: SharedPreferences): SettingsRepository {

    override fun getThemeSettings(): ThemeSettings {

        return ThemeSettings(sharedPrefs.getBoolean(THEME_SHARED_PREFERENCES_KEY, false))

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

    override fun updateThemeSetting(settings: ThemeSettings) {

        sharedPrefs.edit()
            .putBoolean(THEME_SHARED_PREFERENCES_KEY, settings.themeState)
            .apply()
    }
}