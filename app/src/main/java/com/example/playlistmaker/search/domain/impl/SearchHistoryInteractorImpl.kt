package com.example.playlistmaker.search.domain.impl

import com.example.playlistmaker.search.domain.SearchHistoryInteractor
import com.example.playlistmaker.search.domain.SearchHistoryRepository
import com.example.playlistmaker.search.domain.models.Track

class SearchHistoryInteractorImpl(private val repository: SearchHistoryRepository) :

    SearchHistoryInteractor {
    override fun searchHistoryTrack(): List<Track> {
        return repository.searchHistoryTrack()
    }

    override fun searchListFromGson(): List<Track> {
        return repository.searchListFromGson()
    }

    override fun saveSearchList() {
        repository.saveSearchList()
    }

    override fun addTrackHistory(track: Track) {
        repository.addTrackHistory(track)
    }

    override fun searchHistoryClear() {
        repository.searchHistoryClear()
    }
}