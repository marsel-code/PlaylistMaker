package com.example.playlistmaker.domain.api

import com.example.playlistmaker.domain.models.Track

interface SearchHistoryRepository {
    fun searchHistoryTrack(): MutableList<Track>
    fun searchListFromGson(): MutableList<Track>
    fun saveSearchList()
    fun addTrackHistory(track: Track)
    fun searchHistoryClear()
}