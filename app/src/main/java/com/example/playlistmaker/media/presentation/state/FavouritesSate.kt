package com.example.playlistmaker.media.presentation.state

import androidx.annotation.DrawableRes
import com.example.playlistmaker.search.presentation.state.SearchState

sealed interface FavouritesSate {

    data class Error(
        val errorMessage: String,
        @DrawableRes
        val errorImage: Int
    ) : FavouritesSate

    data class Empty(
        val message: String,
        @DrawableRes
        val image: Int
    ) : FavouritesSate


}