package com.example.playlistmaker.search.presentation.state

import androidx.lifecycle.ViewModel
import com.example.playlistmaker.search.domain.models.Track

sealed interface SearchState {
    object Loading : SearchState
    data class Error(val message: String) : SearchState
    data class Content(val data: List<Track>) : SearchState
}