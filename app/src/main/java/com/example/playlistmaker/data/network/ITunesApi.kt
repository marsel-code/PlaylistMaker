package com.example.playlistmaker.data.network

import com.example.playlistmaker.data.dto.ITunesTrackResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface iTunesApi {
    @GET("search")
    fun search(@Query("term") request: String): Call<ITunesTrackResponse>
}


