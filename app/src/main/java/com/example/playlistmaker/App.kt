package com.example.playlistmaker

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.di.dataModule
import com.example.playlistmaker.di.interactorModule
import com.example.playlistmaker.di.repositoryModule
import com.example.playlistmaker.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

const val APP_SHARED_PREFERENCES = "app_shared_preferences"
const val THEME_SHARED_PREFERENCES_KEY = "key_for_dark_theme"

class App : Application() {
    override fun onCreate() {
        super.onCreate()

        val sharedPrefs = getSharedPreferences(APP_SHARED_PREFERENCES, MODE_PRIVATE)
        switchTheme(sharedPrefs.getBoolean(THEME_SHARED_PREFERENCES_KEY, false))

        startKoin {
            androidContext(this@App)
            modules(dataModule, repositoryModule, interactorModule, viewModelModule)
        }
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