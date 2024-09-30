package com.example.playlistmaker.media.presentation.view_model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.R
import com.example.playlistmaker.media.presentation.state.PlayListState

class PlaylistViewModel( private val application: Application): AndroidViewModel(application)  {

    private val stateLiveData = MutableLiveData<PlayListState>()
    fun getLiveDateState(): LiveData<PlayListState> = stateLiveData

    init {
        renderState(
            PlayListState.Empty(
                message = getApplication<Application>().getString(R.string.playlist_empty),
                image = R.drawable.no_mode
            )
        )
    }

    private fun renderState(state: PlayListState) {
        stateLiveData.postValue(state)
    }
}