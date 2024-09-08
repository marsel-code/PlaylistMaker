package com.example.playlistmaker.search.data.network

import com.example.playlistmaker.search.data.dto.ITunesTrackResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface iTunesApi {
    @GET("search")
    suspend fun search(@Query("term") request: String): ITunesTrackResponse
}


