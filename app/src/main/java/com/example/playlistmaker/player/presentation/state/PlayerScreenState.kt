package com.example.playlistmaker.player.presentation.state

import com.example.playlistmaker.search.presentation.model.SearchTrack

sealed class PlayerScreenState {
    data class Content(
        val trackModel: SearchTrack,
        val trackTime: String
    ): PlayerScreenState()
}