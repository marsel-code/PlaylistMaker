package com.example.playlistmaker.media.presentation.state

import androidx.annotation.DrawableRes
import com.example.playlistmaker.media.domain.model.PlayList


sealed interface PlayListState {

    data class Content(
        val playList: List<PlayList>
    ) : PlayListState

    data class Error(
        val errorMessage: String,
        @DrawableRes
        val errorImage: Int
    ) : PlayListState

    data class Empty(
        val message: String,
        @DrawableRes
        val image: Int
    ) : PlayListState


}