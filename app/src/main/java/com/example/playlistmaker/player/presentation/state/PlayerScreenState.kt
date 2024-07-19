package com.example.playlistmaker.player.presentation.state

import com.example.playlistmaker.search.domain.models.Track

sealed class PlayerScreenState {
//    object Loading: PlayerScreenState()
    data class Content(
        val trackModel: Track,
        val trackTime: String
    ): PlayerScreenState()
}