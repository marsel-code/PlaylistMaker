package com.example.playlistmaker.media.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "play_list_table")
class PlayListEntity(
    @PrimaryKey(autoGenerate = true)
    val playListId: Int,
    val playListName: String,
    val playListDescription: String,
    val artworkUrl: String,
    val trackList: String,
    val numberTracks: Long,
)