package com.example.playlistmaker.domain.impl

import com.example.playlistmaker.domain.api.SearchHistoryInteractor
import com.example.playlistmaker.domain.api.SearchHistoryRepository
import com.example.playlistmaker.domain.models.Track

class SearchHistoryInteractorImpl(private val repository: SearchHistoryRepository) :
    SearchHistoryInteractor {
    override fun searchHistoryTrack(): MutableList<Track> {
        return repository.searchHistoryTrack()
    }

    override fun searchListFromGson(): MutableList<Track> {
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