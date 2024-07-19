package com.example.playlistmaker.search.presentation.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.search.presentation.state.SearchState
import com.example.playlistmaker.settings.presentation.state.SettingsState

class SearchViewModel: ViewModel() {

    private var trackInteractor = Creator.provideTracksInteractor()
    private var searchHistory = Creator.provideSearchHistoryInteractor()
    private val state = MutableLiveData<SearchState>()
    fun getSate(): LiveData<SearchState> = state

}