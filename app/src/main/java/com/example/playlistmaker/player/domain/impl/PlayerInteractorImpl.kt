package com.example.playlistmaker.player.domain.impl

import com.example.playlistmaker.player.domain.PlayerInteractor
import com.example.playlistmaker.player.domain.PlayerRepository
import com.example.playlistmaker.player.presentation.state.PlayerState

class PlayerInteractorImpl(private val playerRepository: PlayerRepository) : PlayerInteractor {

    override fun initMediaPlayer(urlTrackPreview: String) {
        playerRepository.initMediaPlayer(urlTrackPreview)
    }

    override fun startPlayer() {
        playerRepository.startPlayer()
    }

    override fun pausePlayer() {
        playerRepository.pausePlayer()
    }

    override fun onPlayButtonClicked() {
        return playerRepository.onPlayButtonClicked()
    }

    override fun playerState(): PlayerState {
        return playerRepository.playerState()
    }

    override fun getCurrentPosition(): String {
        return playerRepository.getCurrentPosition()
    }

    override fun releasePlayer() {
        playerRepository.releasePlayer()
    }
}