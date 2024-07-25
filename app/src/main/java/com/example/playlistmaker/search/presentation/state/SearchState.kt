package com.example.playlistmaker.search.presentation.state

import androidx.annotation.DrawableRes
import com.example.playlistmaker.search.presentation.model.SearchTrack

sealed interface SearchState {

    object Loading : SearchState

    data class Content(
        val tracks: List<SearchTrack>
    ) : SearchState

    data class SaveContent(
        val tracks: List<SearchTrack>
    ) : SearchState

    data class Error(
        val errorMessage: String,
        @DrawableRes
        val errorImage: Int
    ) : SearchState

    data class Empty(
        val message: String,
        @DrawableRes
        val image: Int
    ) : SearchState
}