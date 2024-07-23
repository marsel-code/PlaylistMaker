package com.example.playlistmaker.search.presentation.state

import android.graphics.drawable.Drawable
import com.example.playlistmaker.search.domain.models.Track

sealed interface SearchState {

    object Loading : SearchState

    data class Content(
        val tracks: List<Track>
    ) : SearchState

    data class SaveContent(
        val tracks: List<Track>
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