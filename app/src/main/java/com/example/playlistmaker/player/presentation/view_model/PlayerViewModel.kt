package com.example.playlistmaker.player.presentation.view_model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.player.domain.PlayerInteractor
import com.example.playlistmaker.player.presentation.state.PlayerScreenState
import com.example.playlistmaker.player.presentation.state.PlayerState
import com.example.playlistmaker.search.presentation.model.SearchTrack
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PlayerViewModel(
    private val track: SearchTrack,
    private val trackPlayer: PlayerInteractor
) : ViewModel() {

    companion object {
        const val TRACK_TIME_DELAY = 300L
    }

    private var trackTimeCurrent: String = "00:00"
    private var timerJob: Job? = null
    private val screenStateLiveData =
        MutableLiveData<PlayerScreenState>()

    private val playStatusLiveData = MutableLiveData<PlayerState>()

    init {
        screenStateLiveData.value = PlayerScreenState.Content(track, trackTimeCurrent)
        preparePlayer()
    }

    fun getScreenStateLiveData(): LiveData<PlayerScreenState> = screenStateLiveData
    fun getPlayStatusLiveData(): LiveData<PlayerState> = playStatusLiveData

    fun play() {
        trackPlayer.playbackControl()
        trackTimeUpdate()
        playStatusLiveData.value = trackPlayer.playerState()
    }

    fun preparePlayer() {
        track.previewUrl?.let { trackPlayer.preparePlayer(it) }
    }

    fun playerOnPause() {
        trackPlayer.playerOnPause()
        playStatusLiveData.value = trackPlayer.playerState()
    }

    override fun onCleared() {
        super.onCleared()
        trackPlayer.playerOnDestroy()
        Log.e("Player", "onCleared")
    }

    fun trackTimeUpdate() {
        timerJob = viewModelScope.launch {
            while (trackPlayer.playerState() == PlayerState.STATE_PLAYING) {
                delay(TRACK_TIME_DELAY)
                trackTimeCurrent = trackPlayer.playerGetCurrentPosition()
                screenStateLiveData.postValue(
                    PlayerScreenState.Content(
                        track,
                        trackTimeCurrent
                    )
                )
            }
        }
    }
}