package com.example.playlistmaker.player.presentation.view_model


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.media.domain.FavouriteInteractor
import com.example.playlistmaker.media.domain.PlayListInteractor
import com.example.playlistmaker.media.domain.model.PlayList
import com.example.playlistmaker.player.domain.PlayerInteractor
import com.example.playlistmaker.player.presentation.state.PlayerBottomSheetState
import com.example.playlistmaker.player.presentation.state.PlayerScreenState
import com.example.playlistmaker.player.presentation.state.PlayerState
import com.example.playlistmaker.search.presentation.mapper.SearchTrackMapper
import com.example.playlistmaker.search.presentation.model.SearchTrack
import com.example.playlistmaker.util.SingleLiveEvent
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.lang.reflect.Type

class PlayerViewModel(
    private val track: SearchTrack,
    private val trackPlayer: PlayerInteractor,
    private val favouriteInteractor: FavouriteInteractor,
    private val searchTrackMapper: SearchTrackMapper,
    private val playListInteractor: PlayListInteractor
) : ViewModel() {

    private var timerJob: Job? = null
    val gson = Gson()
    val getType: Type? = object : TypeToken<List<Long>>() {}.type


    private val screenStateLiveData = MutableLiveData<PlayerScreenState.Content>(
        PlayerScreenState.Content(
            track
        )
    )

    fun getScreenStateLiveData(): LiveData<PlayerScreenState.Content> = screenStateLiveData

    private val playerStateLiveData = MutableLiveData<PlayerState>(trackPlayer.playerState())
    fun getPlayerStateLiveData(): LiveData<PlayerState> = playerStateLiveData

    private val playerBottomSheetLiveData = MutableLiveData<PlayerBottomSheetState>()
    fun getPlayerBottomSheetLiveData(): LiveData<PlayerBottomSheetState> =
        playerBottomSheetLiveData

    private val showToast = SingleLiveEvent<String>()
    fun observeShowToast(): LiveData<String> = showToast


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

    fun playListUpdate() {
        viewModelScope.launch {
            playListInteractor
                .getListPlayList()
                .collect { listPlayList ->
                    if (listPlayList.isNotEmpty()) {
                        playerBottomSheetLiveData.postValue(
                            PlayerBottomSheetState.Content(
                                listPlayList
                            )
                        )
                    }
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

    fun checkingTrackPlayList(playList: PlayList, trackSearch: SearchTrack) {

        val tracksIdList = listFromJson(playList.tracksIdList)

        if (tracksIdList.indexOf(trackSearch.trackId) == -1) {
            tracksIdList.add(trackSearch.trackId)
            playList.tracksIdList = listToJson(tracksIdList)
            playList.numberTracks = tracksIdList.size.toLong()
            viewModelScope.launch {
                playListInteractor
                    .updatePlayList(playList)
                playListUpdate()

                playListInteractor
                    .saveTack(searchTrackMapper.mapTrack(trackSearch))

                showToast.postValue(
                    "Добавлено в плейлист ${playList.playListName}"
                )
                playerBottomSheetLiveData.postValue(PlayerBottomSheetState.Nothing())
            }

        } else {
            showToast.postValue(
                "Трек уже добавлен в плейлист ${playList.playListName}"
            )
        }
    }

    fun listFromJson(jsonValue: String): ArrayList<Long> {
        return gson.fromJson(jsonValue, getType)
    }

    fun listToJson(jsonValue: ArrayList<Long>): String {
        return gson.toJson(jsonValue)
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