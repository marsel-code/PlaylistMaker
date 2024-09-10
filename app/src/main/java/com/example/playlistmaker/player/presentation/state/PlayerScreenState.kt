package com.example.playlistmaker.player.presentation.state

import com.example.playlistmaker.search.presentation.model.SearchTrack

sealed class PlayerScreenState(val trackModel: SearchTrack) {
    class Content(trackModel: SearchTrack) : PlayerScreenState(trackModel)
}