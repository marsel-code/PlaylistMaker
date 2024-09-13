package com.example.playlistmaker.search.domain

import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface TracksInteractor {
    fun searchTracks(request: String): Flow<Pair<List<Track>?, String?>>
}