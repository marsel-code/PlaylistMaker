package com.example.playlistmaker.search.domain

import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.util.Resource

interface TracksRepository {
    fun searchTracks(request: String): Resource<List<Track>>
}

