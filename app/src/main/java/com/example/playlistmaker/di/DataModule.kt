package com.example.playlistmaker.di

import android.content.Context
import android.os.FileObserver.CREATE
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.playlistmaker.APP_SHARED_PREFERENCES
import com.example.playlistmaker.media.data.db.AppDatabase
import com.example.playlistmaker.search.data.impl.SearchHistoryImpl
import com.example.playlistmaker.search.data.network.NetworkClient
import com.example.playlistmaker.search.data.network.RetrofitNetworkClient
import com.example.playlistmaker.search.data.network.iTunesApi
import com.example.playlistmaker.search.domain.SearchHistoryRepository
import com.google.gson.Gson
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val dataModule = module {

    single<iTunesApi> {
        Retrofit.Builder()
            .baseUrl("https://itunes.apple.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(iTunesApi::class.java)
    }

    single {
        androidContext()
            .getSharedPreferences(APP_SHARED_PREFERENCES, Context.MODE_PRIVATE)
    }

    factory { Gson() }

    single<NetworkClient> {
        RetrofitNetworkClient(get(), androidContext())
    }

    single {
        Room.databaseBuilder(androidContext(), AppDatabase::class.java, "database.db")
            .addMigrations(MIGRATION_1_2)
            .build()
    }
}

object MIGRATION_1_2 : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL(
            """CREATE TABLE IF NOT EXISTS play_list_table(playListId INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, playListName TEXT NOT NULL, playListDescription TEXT, artworkUri TEXT, tracksIdList TEXT, numberTracks LONG)""".trimIndent())
        database.execSQL(
            """CREATE TABLE IF NOT EXISTS save_track_table(trackId LONG PRIMARY KEY NOT NULL, trackName TEXT NOT NULL, artistName TEXT, trackTimeMillis TEXT, artworkUrl100 TEXT, collectionName TEXT, releaseDate TEXT, primaryGenreName TEXT,country TEXT, previewUrl TEXT, artworkUrl512 TEXT)""".trimIndent())
    }
}


