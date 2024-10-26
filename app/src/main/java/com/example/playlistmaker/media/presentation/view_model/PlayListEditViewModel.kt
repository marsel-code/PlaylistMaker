package com.example.playlistmaker.media.presentation.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.media.domain.PlayListInteractor
import com.example.playlistmaker.media.domain.model.PlayList
import com.example.playlistmaker.media.presentation.state.PlayListEditState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PlayListEditViewModel(
    private val playListId: Int,
    private val playListInteractor: PlayListInteractor
) : PlayListDetailsViewModel(
    playListInteractor
) {

    private lateinit var playList:PlayList

    private val stateLiveData = MutableLiveData<PlayListEditState.Content>()
    fun getLiveDateState(): LiveData<PlayListEditState.Content> = stateLiveData

    fun getPlayList() {
        viewModelScope.launch(Dispatchers.IO) {
            playListInteractor.getPlayList(playListId)
                .collect { getPlayList ->
                    stateLiveData.postValue(PlayListEditState.Content(getPlayList))
                    playList = getPlayList
                }
                    }
    }

    override fun addPlayList(playList: PlayList) {
        viewModelScope.launch(Dispatchers.IO) {
            playList.playListId = playListId

            playListInteractor.addPlayList(playList)
        }
    }
}