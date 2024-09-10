package com.example.playlistmaker.player.presentation.state

sealed class PlayerState(val isPlayButtonEnabled: Boolean, val progress: String) {

    class Default : PlayerState(false, "00:00")

    class Prepared : PlayerState(false, "00:00")

    class Playing(progress: String) : PlayerState(true, progress)

    class Paused(progress: String) : PlayerState(false, progress)
}