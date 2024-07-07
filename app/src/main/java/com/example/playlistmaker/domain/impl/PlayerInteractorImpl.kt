package com.example.playlistmaker.domain.impl

import com.example.playlistmaker.domain.api.PlayerInteractor
import com.example.playlistmaker.domain.api.PlayerRepository
import com.example.playlistmaker.domain.models.PlayerState

class PlayerInteractorImpl(private val playerRepository: PlayerRepository) : PlayerInteractor {

    override fun preparePlayer(urlTrackPreview: String) {
        playerRepository.preparePlayer(urlTrackPreview)
    }

    override fun startPlayer() {
        playerRepository.startPlayer()
    }

    override fun pausePlayer() {
        playerRepository.pausePlayer()
    }

    override fun playbackControl() {
        playerRepository.playbackControl()
    }

    override fun playerState(): PlayerState {
        return playerRepository.playerState()
    }

    override fun playerGetCurrentPosition(): String {
        return playerRepository.playerGetCurrentPosition()
    }

    override fun playerOnPause() {
        playerRepository.playerOnPause()
    }

    override fun playerOnDestroy() {
        playerRepository.playerOnDestroy()
    }
}



