package com.example.playlistmaker.di

import com.example.playlistmaker.media.data.db.FavouriteRepositoryImpl
import com.example.playlistmaker.media.data.db.PlayListRepositoryImpl
import com.example.playlistmaker.media.data.mapper.FavouriteTrackMapper
import com.example.playlistmaker.media.data.mapper.PlayListMapper
import com.example.playlistmaker.media.domain.FavouriteRepository
import com.example.playlistmaker.media.domain.PlayListRepository
import com.example.playlistmaker.player.data.impl.PlayerRepositoryImpl
import com.example.playlistmaker.player.domain.PlayerRepository
import com.example.playlistmaker.search.data.impl.SearchHistoryImpl
import com.example.playlistmaker.search.data.impl.TracksRepositoryImpl
import com.example.playlistmaker.search.domain.SearchHistoryRepository
import com.example.playlistmaker.search.domain.TracksRepository
import com.example.playlistmaker.search.presentation.mapper.SearchTrackMapper
import com.example.playlistmaker.settings.data.impl.SettingsRepositoryImpl
import com.example.playlistmaker.settings.domain.SettingsRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val repositoryModule = module {

    single<TracksRepository> {
        TracksRepositoryImpl(get())
    }

    single<SearchHistoryRepository> {
        SearchHistoryImpl(get(), get())
    }

    factory<PlayerRepository> {
        PlayerRepositoryImpl(androidContext())
    }

    single<SettingsRepository> {
        SettingsRepositoryImpl(get())
    }

    factory { FavouriteTrackMapper() }

    factory { SearchTrackMapper }

    factory { PlayListMapper() }

    single<FavouriteRepository> {
        FavouriteRepositoryImpl(get(), get())
    }

    single<PlayListRepository> {
        PlayListRepositoryImpl(get(), get(), get())
    }




}