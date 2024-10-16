package com.example.playlistmaker.media.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.playlistmaker.media.data.db.entity.SaveTrackEntity



@Dao
interface SaveTracksDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveTrack(track: SaveTrackEntity)

    @Query("DELETE FROM save_track_table WHERE trackId = :id")
    suspend fun deleteTrack(id: Long)

    @Query(
        "SELECT * FROM save_track_table "

    )
    suspend fun getTracks(): List<SaveTrackEntity>

    @Query("SELECT * FROM save_track_table WHERE trackId = :trackId")
    suspend fun getTrackById(trackId: Long): SaveTrackEntity

}