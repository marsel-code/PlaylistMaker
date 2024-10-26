package com.example.playlistmaker.media.presentation.view_model


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.media.domain.PlayListInteractor
import com.example.playlistmaker.media.domain.model.PlayList
import kotlinx.coroutines.launch

open class PlayListDetailsViewModel(
    private val playListInteractor: PlayListInteractor
) : ViewModel() {

    open fun addPlayList(playList: PlayList) {
        viewModelScope.launch {
            playListInteractor.addPlayList(playList)
        }
    }
}