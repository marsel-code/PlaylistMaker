package com.example.playlistmaker.media.presentation.view_model

import android.app.Application

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.R
import com.example.playlistmaker.media.domain.PlayListInteractor
import com.example.playlistmaker.media.domain.model.PlayList
import com.example.playlistmaker.media.presentation.state.PlayListInfoState
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.search.presentation.mapper.SearchTrackMapper
import com.example.playlistmaker.search.presentation.model.SearchTrack
import com.example.playlistmaker.sharing.domain.SharingInteractor
import com.example.playlistmaker.util.SingleLiveEvent
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.reflect.Type

open class PlayListInfoViewModel(
    private val playListInteractor: PlayListInteractor,
    private val mapper: SearchTrackMapper,
    private val sharingInteractor: SharingInteractor,
    private val application: Application
) : AndroidViewModel(application) {

    lateinit var playList: PlayList
    var trackList: List<Track> = ArrayList()

    val gson = Gson()
    val getType: Type? = object : TypeToken<List<Long>>() {}.type

    private val stateLiveData = MutableLiveData<PlayListInfoState>()
    fun getLiveDateState(): LiveData<PlayListInfoState> = stateLiveData

    private val stateLiveDataBottomSheet = MutableLiveData<PlayListInfoState.ContentBottomSheet>()
    fun getLiveDateStateBottomSheet(): LiveData<PlayListInfoState.ContentBottomSheet> =
        stateLiveDataBottomSheet

    private val showToast = SingleLiveEvent<String>()
    fun observeShowToast(): LiveData<String> = showToast

    fun getPlayList(playListId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            playListInteractor.getPlayList(playListId)
                .collect { getPlayList ->
                    stateLiveData.postValue(PlayListInfoState.Content(getPlayList))
                    playList = getPlayList
                }
        }
    }

    fun getTracks(playList: PlayList) {
        viewModelScope.launch(Dispatchers.IO) {
            playListInteractor.getListTrack(listFromJson(playList.tracksIdList))
                .collect { getTrackList ->
                    val sumTracksTime =
                        ((getTrackList.sumOf { trackTimeSecFromInt(it.trackTimeMillis) }) / 60)
                    stateLiveDataBottomSheet.postValue(
                        PlayListInfoState.ContentBottomSheet(
                            playList,
                            mapper.convertFromSearchTrack(getTrackList), sumTracksTime
                        )
                    )
                    trackList = getTrackList
                }
        }
    }

    fun deleteTrack(track: SearchTrack, playListId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            playListInteractor.getPlayList(playListId)
                .collect { playList ->
                    val tracksIdList = listFromJson(playList.tracksIdList)
                    tracksIdList.remove(track.trackId)
                    playList.tracksIdList = listToJson(tracksIdList)
                    playList.numberTracks = tracksIdList.size.toLong()
                    playListInteractor.updatePlayList(playList)
                    getPlayList(playListId)
                }
            playListInteractor.deleteTack(track.trackId)
        }
    }

    fun deletePlayList() {
        val job = viewModelScope.launch(Dispatchers.IO) {
            playListInteractor.deletePlayList(playList)
            playListInteractor.deleteTrackPlayList(playList)
        }
        if (!job.isCompleted) stateLiveData.postValue(PlayListInfoState.Empty())
    }

    fun sharePlayList() {
        if (playList.tracksIdList == "[]") {
            showToast.postValue(
                application.resources.getString(R.string.noTracks)
            )
        } else {
            sharingInteractor.shareText(formatSendText(playList, trackList))
        }
    }

    fun listFromJson(jsonValue: String): ArrayList<Long> {
        return gson.fromJson(jsonValue, getType)
    }

    fun listToJson(jsonValue: ArrayList<Long>): String {
        return gson.toJson(jsonValue)
    }

    fun trackTimeSecFromInt(timeString: String): Int {
        val timeList = timeString.split(":")
        val sumSec = (Integer.parseInt(timeList[0]) * 60) + Integer.parseInt(timeList[1])
        return sumSec
    }

    fun formatSendText(playList: PlayList, trackList: List<Track>): String {
        val sendText = "${playList.playListName}  ${
            if (playList.playListDescription == "") "" else "\n" +
                    playList.playListDescription
        }  \n${
            application.resources.getQuantityString(
                R.plurals.plurals_track,
                playList.numberTracks.toInt(),
                playList.numberTracks.toInt()
            )
        }\n${
            trackList.mapIndexed { index, track -> "${index + 1}. ${track.artistName} - ${track.trackName} (${track.trackTimeMillis}) " }
                .joinToString("\n")
        }"
        return sendText
    }
}

