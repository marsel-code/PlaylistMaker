package com.example.playlistmaker.search.presentation.state

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
        val errorImage: Int
    ) : SearchState

    data class Empty(
        val message: String,
        val image: Int
    ) : SearchState
}