package com.example.playlistmaker.settings.domain.impl

import com.example.playlistmaker.settings.domain.SettingsInteractor
import com.example.playlistmaker.settings.domain.SettingsRepository
import com.example.playlistmaker.settings.domain.model.ThemeSettings

class SettingsInteractorImpl(private val settingsRepository: SettingsRepository) :
    SettingsInteractor {
    override fun getThemeSettings(): ThemeSettings {
       return settingsRepository.getThemeSettings()
    }

    override fun updateThemeSetting(settings: ThemeSettings) {
        settingsRepository.updateThemeSetting(settings)
    }

}