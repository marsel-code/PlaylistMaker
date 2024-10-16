package com.example.playlistmaker.media.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "save_track_table")
data class SaveTrackEntity (
    @PrimaryKey()
    val trackId: Long,
    val trackName: String?,
    val artistName: String?,
    val trackTimeMillis: String,
    val artworkUrl100: String?,
    val collectionName: String?,
    val releaseDate: String?,
    val primaryGenreName: String?,
    val country: String?,
    val previewUrl: String?,
    val artworkUrl512: String?
)