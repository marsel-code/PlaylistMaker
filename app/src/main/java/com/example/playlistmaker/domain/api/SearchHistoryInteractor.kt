package com.example.playlistmaker.domain.api

import com.example.playlistmaker.domain.models.Track

interface SearchHistoryInteractor {
    fun searchHistoryTrack(): List<Track>
    fun searchListFromGson(): List<Track>
    fun saveSearchList()
    fun addTrackHistory(track: Track)
    fun searchHistoryClear()
}