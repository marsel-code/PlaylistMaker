package com.example.playlistmaker.player.presentation.view_model

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.room.util.copy
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.player.domain.PlayerInteractor
import com.example.playlistmaker.player.presentation.state.PlayerScreenState
import com.example.playlistmaker.player.presentation.state.PlayerState
import com.example.playlistmaker.search.domain.TracksInteractor
import com.example.playlistmaker.search.domain.models.Track

class PlayerViewModel(
    private val track: Track,
    private val trackPlayer: PlayerInteractor
) : ViewModel() {
    private val TRACK_TIME_DELAY = 300L
    private var trackTimeCurrent: String = ""
    private val handler = Handler(Looper.getMainLooper())
    private var screenStateLiveData =
        MutableLiveData<PlayerScreenState>()

    private val playStatusLiveData = MutableLiveData<PlayerState>()

    init {
        track.previewUrl?.let { preparePlayer(it) }
        screenStateLiveData.value = PlayerScreenState.Content(track, trackTimeCurrent)
        screenStateLiveData.postValue(PlayerScreenState.Content(track, trackTimeCurrent))

    }

    fun getScreenStateLiveData(): LiveData<PlayerScreenState> = screenStateLiveData
    fun getPlayStatusLiveData(): LiveData<PlayerState> = playStatusLiveData

    fun play() {
        trackPlayer.playbackControl()
        trackTimeUpdate()
        playStatusLiveData.value = trackPlayer.playerState()
    }

    fun preparePlayer(trackURL: String) {
        trackPlayer.preparePlayer(trackURL)
    }

    fun playerOnPause() {
        trackPlayer.playerOnPause()
        playStatusLiveData.value = trackPlayer.playerState()
    }

    fun playerOnDestroy() {
        trackPlayer.playerOnDestroy()
    }

    override fun onCleared() {
        trackPlayer.playerOnDestroy()
        handler.removeCallbacksAndMessages(null)
    }

    fun trackTimeUpdate() {
        handler.postDelayed(
            object : Runnable {
                override fun run() {
                    trackTimeCurrent = trackPlayer.playerGetCurrentPosition()
                    screenStateLiveData.postValue(PlayerScreenState.Content(track, trackTimeCurrent))
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
        fun getViewModelFactory(track: Track?): ViewModelProvider.Factory = viewModelFactory {
            val trackPlayer = Creator.providePlayerInteractor()
            initializer {
                track?.let {
                    PlayerViewModel(
                        it,
                        trackPlayer
                    )
                }!!
            }
        }


    }
}