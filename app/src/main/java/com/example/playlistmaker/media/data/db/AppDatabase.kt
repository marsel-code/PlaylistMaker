package com.example.playlistmaker.media.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.playlistmaker.media.data.db.dao.PlayListDao
import com.example.playlistmaker.media.data.db.dao.SaveTracksDao
import com.example.playlistmaker.media.data.db.dao.TrackDao
import com.example.playlistmaker.media.data.db.entity.PlayListEntity
import com.example.playlistmaker.media.data.db.entity.SaveTrackEntity
import com.example.playlistmaker.media.data.db.entity.TrackEntity


@Database(
    version = 2,
    entities = [TrackEntity::class, PlayListEntity::class, SaveTrackEntity::class]
)

abstract class AppDatabase : RoomDatabase() {
    abstract fun trackDao(): TrackDao
    abstract fun playListDao(): PlayListDao
    abstract fun saveTrackDao(): SaveTracksDao
}