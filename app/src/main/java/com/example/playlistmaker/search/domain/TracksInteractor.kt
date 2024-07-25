package com.example.playlistmaker.search.domain

import com.example.playlistmaker.search.domain.models.Track

interface TracksInteractor {
    fun searchTracks(request: String, consumer: TracksConsumer)

    interface TracksConsumer {
        fun consume(foundTracks: List<Track>?, errorMessage: String?)
    }
}