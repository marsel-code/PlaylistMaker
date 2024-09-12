package com.example.playlistmaker.player.presentation.state

sealed class PlayerState(val isPlayButtonEnabled: Boolean, val progress: String) {

    class Default(progress: String) : PlayerState(false, progress)

    class Prepared(progress: String) : PlayerState(false, progress)

    class Playing(progress: String) : PlayerState(true, progress)

    class Paused(progress: String) : PlayerState(false, progress)
}