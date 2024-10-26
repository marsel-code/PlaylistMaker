package com.example.playlistmaker.media.presentation.state

import com.example.playlistmaker.media.domain.model.PlayList

interface PlayListEditState {
    data class Content(
        val playList: PlayList
    ) : PlayListEditState
}