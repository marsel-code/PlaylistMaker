package com.example.playlistmaker

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface iTunesApi {
    @GET("search")
    fun search(@Query("term") text: String): Call<iTunesTrackResponse>
}


