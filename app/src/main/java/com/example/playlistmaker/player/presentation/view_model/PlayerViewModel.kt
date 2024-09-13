package com.example.playlistmaker.player.presentation.view_model

import android.media.MediaPlayer
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
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerViewModel(
    track: SearchTrack,
    private val trackPlayer: PlayerInteractor
) : ViewModel() {

    companion object {
        const val TRACK_TIME_DELAY = 300L
    }

    private var timerJob: Job? = null

    private val screenStateLiveData = MutableLiveData<PlayerScreenState.Content>(
        PlayerScreenState.Content(
            track
        )
    )

    fun getScreenStateLiveData(): LiveData<PlayerScreenState.Content> = screenStateLiveData

    private val playerStateLiveData = MutableLiveData<PlayerState>(trackPlayer.playerState())
    fun getPlayerStateLiveData(): LiveData<PlayerState> = playerStateLiveData

    init {
        track.previewUrl?.let { trackPlayer.initMediaPlayer(it) }
    }

    fun onPause() {
        pausePlayer()
    }

    fun onPlayButtonClicked() {
        trackPlayer.onPlayButtonClicked()
        playerStateLiveData.postValue(trackPlayer.playerState())
        timerJob?.cancel()
        startTimer()
    }

    private fun pausePlayer() {
        trackPlayer.pausePlayer()
        timerJob?.cancel()
        playerStateLiveData.postValue(trackPlayer.playerState())
    }

    private fun releasePlayer() {
        trackPlayer.releasePlayer()
        playerStateLiveData.value = trackPlayer.playerState()
    }

    private fun startTimer() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (trackPlayer.playerState() is PlayerState.Playing) {
                delay(TRACK_TIME_DELAY)
                playerStateLiveData.postValue(trackPlayer.playerState())
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        releasePlayer()
    }
}