package com.example.playlistmaker.media.presentation.view_model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.media.domain.PlayListInteractor
import com.example.playlistmaker.media.domain.model.PlayList
import kotlinx.coroutines.launch

class PlayListDetailsViewModel(
    private val playListInteractor: PlayListInteractor
) : ViewModel() {


    fun addPlayList(playList: PlayList) {
        viewModelScope.launch {
playListInteractor.addPlayList(playList)
        }
    }


}