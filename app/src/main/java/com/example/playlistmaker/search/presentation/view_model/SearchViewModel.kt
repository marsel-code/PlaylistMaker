package com.example.playlistmaker.search.presentation.view_model


import android.app.Application
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.R
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.util.SingleLiveEvent
import com.example.playlistmaker.search.domain.TracksInteractor
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.search.presentation.mapper.SearchTrackMapper
import com.example.playlistmaker.search.presentation.model.SearchTrack
import com.example.playlistmaker.search.presentation.state.SearchState

class SearchViewModel(application: Application) : AndroidViewModel(application) {

    private val handler = Handler(Looper.getMainLooper())

    private var trackInteractor = Creator.provideTracksInteractor()
    private var searchHistory = Creator.provideSearchHistoryInteractor()


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
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)
        val searchRunnable = Runnable { search(changedText) }
        val postTime = SystemClock.uptimeMillis() + SEARCH_DEBOUNCE_DELAY
        handler.postAtTime(
            searchRunnable,
            SEARCH_REQUEST_TOKEN,
            postTime,
        )
    }

    fun search(newSearchText: String) {
        if (newSearchText.isNotEmpty()) {
            renderState((SearchState.Loading))
            trackInteractor.searchTracks(
                newSearchText,
                object : TracksInteractor.TracksConsumer {
                    override fun consume(foundTracks: List<Track>?, errorMessage: String?) {
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
                                showToast.postValue("ошибка связи")
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
                }
            )
        }
    }

    override fun onCleared() {
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)
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
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)
        val searchTracksGson = searchHistory.searchListFromGson()
        renderState(SearchState.SaveContent(searchTracksGson.map { track ->
            SearchTrackMapper.mapSearchTrack(track)
        }))
    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private val SEARCH_REQUEST_TOKEN = Any()
        fun getViewModelFactory(): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                SearchViewModel(this[APPLICATION_KEY] as Application)
            }
        }
    }
}