package com.example.playlistmaker.media.presentation.view_model

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.R
import com.example.playlistmaker.media.domain.db.FavouriteInteractor
import com.example.playlistmaker.media.presentation.state.FavouritesSate
import com.example.playlistmaker.player.presentation.state.PlayerScreenState
import com.example.playlistmaker.search.presentation.mapper.SearchTrackMapper
import kotlinx.coroutines.launch

class FavouritesViewModel(
    private val favouriteInteractor: FavouriteInteractor,
    private val searchTrackMapper: SearchTrackMapper,
    private val application: Application
) : AndroidViewModel(application) {

    private val stateLiveData = MutableLiveData<FavouritesSate>(FavouritesSate.Loading)
    fun getLiveDateState(): LiveData<FavouritesSate> = stateLiveData


    init {
        renderState()
    }

    fun renderState() {
        viewModelScope.launch {
            favouriteInteractor
                .getFavouriteTracks()
                .collect { tracks ->
                    if (tracks.isNotEmpty()) {
                        stateLiveData.postValue(
                            FavouritesSate.Content(
                                tracks.map { track -> searchTrackMapper.mapSearchTrack(track) }
                            )
                        )

                    } else {
                        stateLiveData.postValue(
                            FavouritesSate.Empty(
                                message = getApplication<Application>().getString(R.string.favourites_empty),
                                image = R.drawable.no_mode
                            )
                        )
                    }
                }
        }
    }
}

