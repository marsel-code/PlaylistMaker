package com.example.playlistmaker.media.presentation.state

import androidx.annotation.DrawableRes

sealed interface PlayListState {

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