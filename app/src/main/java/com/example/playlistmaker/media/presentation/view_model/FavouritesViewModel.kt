package com.example.playlistmaker.media.presentation.view_model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.playlistmaker.R
import com.example.playlistmaker.media.presentation.state.FavouritesSate

class FavouritesViewModel(private val application: Application): AndroidViewModel(application) {

    private val stateLiveData = MutableLiveData<FavouritesSate>()
    fun getLiveDateState(): LiveData<FavouritesSate> = stateLiveData

    init {
        renderState(
            FavouritesSate.Empty(
                message = getApplication<Application>().getString(R.string.favourites_empty),
                image = R.drawable.no_mode
            )
        )
    }

    private fun renderState(state: FavouritesSate) {
        stateLiveData.postValue(state)
    }
}
