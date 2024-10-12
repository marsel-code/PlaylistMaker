package com.example.playlistmaker.media.domain.model

data class PlayList(
    val playListId: Int,
    val playListName: String,
    val playListDescription: String,
    val artworkUrl: String,
    val trackList: String,
    val numberTracks: Long,
)
