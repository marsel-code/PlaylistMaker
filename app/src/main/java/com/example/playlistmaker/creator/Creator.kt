package com.example.playlistmaker.creator

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.example.playlistmaker.APP_SHARED_PREFERENCES
import com.example.playlistmaker.player.data.impl.PlayerRepositoryImpl
import com.example.playlistmaker.search.data.impl.SearchHistoryImpl
import com.example.playlistmaker.search.data.impl.TracksRepositoryImpl
import com.example.playlistmaker.search.data.network.RetrofitNetworkClient
import com.example.playlistmaker.player.domain.PlayerInteractor
import com.example.playlistmaker.player.domain.PlayerRepository
import com.example.playlistmaker.search.domain.SearchHistoryInteractor
import com.example.playlistmaker.search.domain.SearchHistoryRepository
import com.example.playlistmaker.search.domain.TracksInteractor
import com.example.playlistmaker.search.domain.TracksRepository
import com.example.playlistmaker.player.domain.impl.PlayerInteractorImpl
import com.example.playlistmaker.search.domain.impl.SearchHistoryInteractorImpl
import com.example.playlistmaker.search.domain.impl.TracksInteractorImpl
import com.example.playlistmaker.settings.data.impl.SettingsRepositoryImpl
import com.example.playlistmaker.settings.domain.SettingsInteractor
import com.example.playlistmaker.settings.domain.SettingsRepository
import com.example.playlistmaker.settings.domain.impl.SettingsInteractorImpl
import com.example.playlistmaker.settings.domain.impl.SharingInteractorImpl
import com.example.playlistmaker.sharing.data.impl.ExternalNavigatorImpl
import com.example.playlistmaker.sharing.domain.ExternalNavigator
import com.example.playlistmaker.sharing.domain.SharingInteractor
import com.google.gson.Gson

object Creator {
//    private lateinit var application: Application
//
//    fun initApplication(application: Application) {
//        this.application = application
//    }
//    private fun getTracksRepository(): TracksRepository {
//        return TracksRepositoryImpl(RetrofitNetworkClient(application))
//    }
//
//    fun provideTracksInteractor(): TracksInteractor {
//        return TracksInteractorImpl(getTracksRepository())
//    }
//    private fun getPlayerRepository(): PlayerRepository {
//        return PlayerRepositoryImpl()
//    }
//
//    fun providePlayerInteractor(): PlayerInteractor {
//        return PlayerInteractorImpl(getPlayerRepository())
//    }
//    private fun getSearchHistoryRepository(): SearchHistoryRepository {
//        return SearchHistoryImpl(providePreference(), Gson())
//    }
////    fun provideSearchHistoryInteractor(): SearchHistoryInteractor {
//        return SearchHistoryInteractorImpl(getSearchHistoryRepository())
//    }
//    fun providePreference(): SharedPreferences {
//        return application.getSharedPreferences(APP_SHARED_PREFERENCES, Context.MODE_PRIVATE)
//    }
//
//    private fun getSettingsRepository(): SettingsRepository {
//        return SettingsRepositoryImpl(providePreference())
//    }
//    fun provideSettingsInteractor(): SettingsInteractor {
//        return SettingsInteractorImpl(getSettingsRepository())
//    }
//
//    fun provideSharingInteractor(): SharingInteractor {
//        return SharingInteractorImpl(getExternalNavigator())
//    }
//
//    private fun getExternalNavigator(): ExternalNavigator {
//        return ExternalNavigatorImpl(application)
//    }
}