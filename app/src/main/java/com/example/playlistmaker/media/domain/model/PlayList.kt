package com.example.playlistmaker.media.domain.model

data class PlayList(
    val playListId: Int,
    val playListName: String,
    val playListDescription: String,
    val artworkUri: String,
    var numberTracks: Long,
    var tracksIdList: String,

    )
