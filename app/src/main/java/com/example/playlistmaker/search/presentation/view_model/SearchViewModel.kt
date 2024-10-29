package com.example.playlistmaker.search.presentation.view_model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.R
import com.example.playlistmaker.search.domain.SearchHistoryInteractor
import com.example.playlistmaker.util.SingleLiveEvent
import com.example.playlistmaker.search.domain.TracksInteractor
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.search.presentation.mapper.SearchTrackMapper
import com.example.playlistmaker.search.presentation.model.SearchTrack
import com.example.playlistmaker.search.presentation.state.SearchState
import com.example.playlistmaker.util.debounce
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SearchViewModel(
    private var trackInteractor: TracksInteractor,
    private var searchHistory: SearchHistoryInteractor,
    application: Application
) : AndroidViewModel(application) {

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }

    private val trackSearchDebounce =
        debounce<String>(SEARCH_DEBOUNCE_DELAY, viewModelScope, true) { changedText ->
            search(changedText)
        }

    private val stateLiveData = MutableLiveData<SearchState>()
    fun getLiveDateState(): LiveData<SearchState> = stateLiveData

    private val showToast = SingleLiveEvent<String>()
    fun observeShowToast(): LiveData<String> = showToast

    var latestSearchText: String? = null

    fun searchDebounce(changedText: String) {
        if (latestSearchText == changedText) {
            return
        }
        this.latestSearchText = changedText
        trackSearchDebounce(changedText)
    }

    fun search(newSearchText: String) {
        if (newSearchText.isNotEmpty()) {
            renderState((SearchState.Loading))

            viewModelScope.launch(Dispatchers.IO) {
                trackInteractor
                    .searchTracks(newSearchText)
                    .collect { pair ->
                        processResult(pair.first, pair.second)
                    }
            }
        }
    }

    private fun processResult(foundTracks: List<Track>?, errorMessage: String?) {
        val tracks = mutableListOf<SearchTrack>()
        if (foundTracks != null) {
            tracks.addAll(foundTracks.map { track ->
                SearchTrackMapper.mapSearchTrack(track)
            })
        }
        when {
            errorMessage != null -> {
                renderState(
                    SearchState.Error(
                        errorMessage = getApplication<Application>().getString(
                            R.string.something_went_wrong
                        ),
                        errorImage = R.drawable.error_image
                    )
                )
                latestSearchText = null
                showToast.postValue(
                    getApplication<Application>().getString(
                        R.string.no_connection
                    )
                )
            }

            tracks.isEmpty() -> {
                renderState(
                    SearchState.Empty(
                        message = getApplication<Application>().getString(R.string.nothing_found),
                        image = R.drawable.no_mode
                    )
                )
            }

            else -> {
                renderState(SearchState.Content(tracks))
            }
        }
    }

    override fun onCleared() {

    }

    private fun renderState(state: SearchState) {
        stateLiveData.postValue(state)
    }

    fun saveTrack(track: SearchTrack) {
        searchHistory.addTrackHistory(SearchTrackMapper.mapTrack(track))
        searchHistory.saveSearchList()
    }

    fun searchHistoryClear() {
        searchHistory.searchHistoryClear()
    }

    fun getSaveSearchList() {
        val searchTracksGson = searchHistory.searchListFromGson()
        renderState(SearchState.SaveContent(searchTracksGson.map { track ->
            SearchTrackMapper.mapSearchTrack(track)
        }))
    }
}