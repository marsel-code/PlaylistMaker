package com.example.playlistmaker.domain.api

import com.example.playlistmaker.domain.models.PlayerState

interface PlayerInteractor {
    fun preparePlayer(urlTrackPreview: String)
    fun startPlayer()
    fun pausePlayer()
    fun playbackControl()
    fun playerState(): PlayerState
    fun playerGetCurrentPosition(): String
    fun playerOnPause()
    fun playerOnDestroy()
}