package com.example.playlistmaker.player.domain

import com.example.playlistmaker.player.presentation.state.PlayerState

interface PlayerRepository {
    fun initMediaPlayer(urlTrackPreview: String)
    fun startPlayer()
    fun pausePlayer()
    fun onPlayButtonClicked()
    fun playerState(): PlayerState
    fun getCurrentPosition(): String
    fun releasePlayer()
}