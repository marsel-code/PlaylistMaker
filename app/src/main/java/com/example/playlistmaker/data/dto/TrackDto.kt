package com.example.playlistmaker.data.dto

import java.text.SimpleDateFormat
import java.util.Locale

data class TrackDto(
    val trackId: Long,
    val trackName: String?,
    val artistName: String?,
    val trackTimeMillis: Long,
    val artworkUrl100: String?,
    val collectionName: String?,
    val releaseDate: String?,
    val primaryGenreName: String?,
    val country: String?,
    val previewUrl: String?
) {
    private val timeFormat by lazy { SimpleDateFormat("mm:ss", Locale.getDefault()) }

    fun trackTime(): String {
        return timeFormat.format(trackTimeMillis)
    }
}