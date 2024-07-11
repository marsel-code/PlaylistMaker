package com.example.playlistmaker.player.domain

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