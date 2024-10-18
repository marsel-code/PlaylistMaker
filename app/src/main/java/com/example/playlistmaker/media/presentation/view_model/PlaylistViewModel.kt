package com.example.playlistmaker.media.presentation.view_model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.R
import com.example.playlistmaker.media.domain.PlayListInteractor
import com.example.playlistmaker.media.presentation.state.PlayListState
import kotlinx.coroutines.launch

class PlaylistViewModel(
    private val application: Application, private val playListInteractor: PlayListInteractor
) : AndroidViewModel(application) {

    private val stateLiveData = MutableLiveData<PlayListState>()
    fun getLiveDateState(): LiveData<PlayListState> = stateLiveData

    init {
        renderState()
    }

    fun renderState() {
        viewModelScope.launch {
            playListInteractor
                .getListPlayList()
                   .collect { listPlayList ->
                    if (listPlayList.isNotEmpty()) {
                        stateLiveData.postValue(
                            PlayListState.Content(
                                listPlayList
                            )
                        )

                    } else {
                        stateLiveData.postValue(
                            PlayListState.Empty(
                                message = getApplication<Application>().getString(R.string.playlist_empty),
                                image = R.drawable.no_mode
                            )
                        )
                    }
                }
        }
    }




}