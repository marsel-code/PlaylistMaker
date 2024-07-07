package com.example.playlistmaker.data

import android.content.SharedPreferences
import com.example.playlistmaker.domain.api.SearchHistoryRepository
import com.example.playlistmaker.domain.models.Track
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class SearchHistoryImpl(private val sharedPrefsSearch: SharedPreferences) :
    SearchHistoryRepository {

    private var searchHistory: MutableList<Track> = mutableListOf()

    override fun searchHistoryTrack(): MutableList<Track> {
        return searchHistory
    }

    override fun searchListFromGson(): MutableList<Track> {
        val gsonFrom = sharedPrefsSearch.getString(SEARCH_SHARED_PREFERENCES_KEY, null)
        val type = object : TypeToken<MutableList<Track>>() {}.type
        if (gsonFrom != null) {
            searchHistory = Gson().fromJson(gsonFrom, type)
            return searchHistory
        }
        return mutableListOf()
    }

    override fun saveSearchList() {
        val trackGson = Gson().toJson(searchHistory)
        sharedPrefsSearch.edit()
            .putString(SEARCH_SHARED_PREFERENCES_KEY, trackGson)
            .apply()
    }


    override fun addTrackHistory(track: Track) {
        val interator = searchHistory.iterator()
        while (interator.hasNext()) {
            if (interator.next().trackId == track.trackId) interator.remove()
        }
        if (searchHistory.isNotEmpty() && searchHistory.size == 10) searchHistory.removeAt(9)
        searchHistory.add(0, track)
    }

    override fun searchHistoryClear() {
        searchHistory.clear()
        saveSearchList()
    }


    companion object {
        const val SEARCH_SHARED_PREFERENCES_KEY = "key_for_search"
    }
}