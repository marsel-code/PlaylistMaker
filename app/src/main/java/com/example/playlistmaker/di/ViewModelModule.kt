package com.example.playlistmaker.di

import com.example.playlistmaker.media.data.mapper.FavouriteTrackMapper
import com.example.playlistmaker.media.presentation.view_model.FavouritesViewModel
import com.example.playlistmaker.media.presentation.view_model.PlaylistViewModel
import com.example.playlistmaker.player.presentation.view_model.PlayerViewModel
import com.example.playlistmaker.search.presentation.mapper.SearchTrackMapper
import com.example.playlistmaker.search.presentation.model.SearchTrack
import com.example.playlistmaker.search.presentation.view_model.SearchViewModel
import com.example.playlistmaker.settings.presentation.view_model.SettingViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {

    viewModel {
        SearchViewModel(get(), get(), get())
    }

    viewModel { (track: SearchTrack) ->
        PlayerViewModel(track, get(), get(), get() )
    }

    viewModel {
        SettingViewModel(get(), get(), get())
    }

    viewModel {
        PlaylistViewModel(get())
    }

    viewModel {
        FavouritesViewModel(get(), get(), get())
    }



}