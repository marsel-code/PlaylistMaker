package com.example.playlistmaker.player.presentation.view_model

import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.player.domain.PlayerInteractor
import com.example.playlistmaker.player.presentation.state.PlayerScreenState
import com.example.playlistmaker.player.presentation.state.PlayerState
import com.example.playlistmaker.search.presentation.model.SearchTrack

class PlayerViewModel(
    private val track: SearchTrack,
    private val trackPlayer: PlayerInteractor
) : ViewModel() {

    private var trackTimeCurrent: String = "00:00"
    private val handler = Handler(Looper.getMainLooper())

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
        handler.removeCallbacksAndMessages(null)
        playStatusLiveData.value = trackPlayer.playerState()
        }

    fun playerOnDestroy() {
        trackPlayer.playerOnDestroy()
    }

    override fun onCleared() {
        super.onCleared()
        trackPlayer.playerOnDestroy()
        handler.removeCallbacksAndMessages(null)
        Log.e("Player", "onCleared")
    }

    fun trackTimeUpdate() {
        handler.postDelayed(
            object : Runnable {
                override fun run() {
                    trackTimeCurrent = trackPlayer.playerGetCurrentPosition()
                    screenStateLiveData.postValue(
                        PlayerScreenState.Content(
                            track,
                            trackTimeCurrent
                        )
                    )
                    handler.postDelayed(
                        this,
                        TRACK_TIME_DELAY,
                    )
                }
            },
            TRACK_TIME_DELAY
        )
    }

    companion object {
        const val TRACK_TIME_DELAY = 300L
    }
}