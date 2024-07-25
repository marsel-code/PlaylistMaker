package com.example.playlistmaker.player.domain

import com.example.playlistmaker.player.presentation.state.PlayerState

interface PlayerRepository {
    fun preparePlayer(urlTrackPreview: String)
    fun startPlayer()
    fun pausePlayer()
    fun playbackControl()
    fun playerState(): PlayerState
    fun playerGetCurrentPosition(): String
    fun playerOnPause()
    fun playerOnDestroy()
}