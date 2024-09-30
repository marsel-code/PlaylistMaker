package com.example.playlistmaker.player.presentation.view_model

import android.media.MediaPlayer
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.media.domain.db.FavouriteInteractor
import com.example.playlistmaker.player.domain.PlayerInteractor
import com.example.playlistmaker.player.presentation.state.PlayerScreenState
import com.example.playlistmaker.player.presentation.state.PlayerState
import com.example.playlistmaker.search.presentation.mapper.SearchTrackMapper
import com.example.playlistmaker.search.presentation.model.SearchTrack
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerViewModel(
    private val track: SearchTrack,
    private val trackPlayer: PlayerInteractor,
    private val favouriteInteractor: FavouriteInteractor,
    private val searchTrackMapper: SearchTrackMapper
) : ViewModel() {

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

    fun checkingTrackFavourites() {
        viewModelScope.launch {
            favouriteInteractor
                .getFavouriteTracksId(searchTrackMapper.mapTrack(track))
                .collect { trackId ->
                    Log.d("favourites", trackId.trackId.toString())
                    if (trackId.trackId == track.trackId) {
                        track.isFavorite = true
                        screenStateLiveData.postValue(
                            PlayerScreenState.Content(
                                track
                            )
                        )
                    }
                }
        }
    }

    fun onFavoriteClicked() {
        viewModelScope.launch {
            if (!track.isFavorite) {
                track.isFavorite = true
                screenStateLiveData.postValue(
                    PlayerScreenState.Content(
                        track
                    )
                )
                favouriteInteractor.addTrack(searchTrackMapper.mapTrack(track))
            } else {
                track.isFavorite = false
                screenStateLiveData.postValue(
                    PlayerScreenState.Content(
                        track
                    )
                )
                favouriteInteractor.deleteTrack(track.trackId)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        releasePlayer()
    }

    companion object {
        const val TRACK_TIME_DELAY = 300L
    }
}