package com.example.playlistmaker.player.presentation.state

import com.example.playlistmaker.media.domain.model.PlayList


sealed class PlayerBottomSheetState {
    class Nothing():PlayerBottomSheetState()
    class Content(val listPlayList: List<PlayList>) : PlayerBottomSheetState()
}