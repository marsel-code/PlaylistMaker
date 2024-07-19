package com.example.playlistmaker.settings.presentation.view_model


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.settings.domain.SettingsInteractor
import com.example.playlistmaker.settings.presentation.state.SettingsState
import com.example.playlistmaker.sharing.domain.SharingInteractor

class SettingViewModel(
    private val sharingInteractor: SharingInteractor,
    private val settingsInteractor: SettingsInteractor
) : ViewModel() {

private val state = MutableLiveData<SettingsState>()

fun getSate(): LiveData<SettingsState> = state



}