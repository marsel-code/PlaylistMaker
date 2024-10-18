package com.example.playlistmaker.media.presentation.state

import androidx.annotation.DrawableRes
import com.example.playlistmaker.search.presentation.model.SearchTrack


sealed interface FavouritesSate {

    object Loading : FavouritesSate

    data class Content(
        val tracks: List<SearchTrack>
    ) : FavouritesSate

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