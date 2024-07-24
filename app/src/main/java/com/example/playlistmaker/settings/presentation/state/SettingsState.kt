package com.example.playlistmaker.settings.presentation.state

 sealed interface SettingsState {
     data class StatusObserver(var switch: Boolean): SettingsState
}



