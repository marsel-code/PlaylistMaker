package com.example.playlistmaker.settings.presentation.view_model


import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.App
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.settings.domain.model.ThemeSettings
import com.example.playlistmaker.settings.presentation.state.SettingsState

class SettingViewModel(private val application: Application) : AndroidViewModel(application) {

    private val getSettingsInteractor = Creator.provideSettingsInteractor()
    private val getSharingInteractor = Creator.provideSharingInteractor()

    private val stateSettingsLiveData = MutableLiveData<SettingsState>()

    init {
        stateSettingsLiveData.postValue(SettingsState.StatusObserver(getSettingsInteractor.getThemeSettings().themeState))
    }

    fun getSate(): LiveData<SettingsState> = stateSettingsLiveData

    fun shareButton() {
        getSharingInteractor.openTerms()
    }

    fun supportButton() {
        getSharingInteractor.openSupport()
    }

    fun agreementUserButton() {
        getSharingInteractor.shareApp()
    }

    fun checkedChangeListener(checked: Boolean) {
        getSettingsInteractor.updateThemeSetting(ThemeSettings(checked))
        stateSettingsLiveData.postValue(SettingsState.StatusObserver(checked))
        (application as App).switchTheme(checked)
    }

    companion object {
        fun getViewModelFactory(): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                SettingViewModel(this[APPLICATION_KEY] as Application)
            }
        }
    }
}