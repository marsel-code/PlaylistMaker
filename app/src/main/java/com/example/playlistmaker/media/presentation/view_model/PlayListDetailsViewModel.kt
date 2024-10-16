package com.example.playlistmaker.media.presentation.view_model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.media.domain.db.PlayListInteractor
import com.example.playlistmaker.media.domain.model.PlayList
import kotlinx.coroutines.launch

class PlayListDetailsViewModel(
    private val application: Application,
    private val playListInteractor: PlayListInteractor
) : AndroidViewModel(application) {


    fun addPlayList(playList: PlayList) {
        viewModelScope.launch {
playListInteractor.addPlayList(playList)
        }
    }


}