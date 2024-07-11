package com.example.playlistmaker.search.domain

import com.example.playlistmaker.search.domain.models.Track

interface SearchHistoryInteractor {
    fun searchHistoryTrack(): List<Track>
    fun searchListFromGson(): List<Track>
    fun saveSearchList()
    fun addTrackHistory(track: Track)
    fun searchHistoryClear()
}