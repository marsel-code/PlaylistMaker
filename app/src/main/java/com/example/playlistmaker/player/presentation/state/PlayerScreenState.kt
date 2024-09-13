package com.example.playlistmaker.player.presentation.state

import com.example.playlistmaker.search.presentation.model.SearchTrack

sealed class PlayerScreenState() {
    class Content(val trackModel: SearchTrack) : PlayerScreenState()
}