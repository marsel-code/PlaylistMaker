package com.example.playlistmaker.settings.presentation.view_model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.playlistmaker.App
import com.example.playlistmaker.settings.domain.SettingsInteractor
import com.example.playlistmaker.settings.domain.model.ThemeSettings
import com.example.playlistmaker.settings.presentation.state.SettingsState
import com.example.playlistmaker.sharing.domain.SharingInteractor

class SettingViewModel(
    private val application: Application,
    private val settingsInteractor: SettingsInteractor,
    private val sharingInteractor: SharingInteractor
) : AndroidViewModel(application) {

    private val stateSettingsLiveData = MutableLiveData<SettingsState>()

    init {
        stateSettingsLiveData.postValue(SettingsState.StatusObserver(this.settingsInteractor.getThemeSettings().themeState))
    }

    fun getSate(): LiveData<SettingsState> = stateSettingsLiveData

    fun shareButton() {
        sharingInteractor.openTerms()
    }

    fun supportButton() {
        sharingInteractor.openSupport()
    }

    fun agreementUserButton() {
        sharingInteractor.shareApp()
    }

    fun checkedChangeListener(checked: Boolean) {
        settingsInteractor.updateThemeSetting(ThemeSettings(checked))
        stateSettingsLiveData.postValue(SettingsState.StatusObserver(checked))
        (application as App).switchTheme(checked)
    }
}