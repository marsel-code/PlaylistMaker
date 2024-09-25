package com.example.playlistmaker.media.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.playlistmaker.media.data.db.entity.TrackEntity


@Dao
interface TrackDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrack(track: TrackEntity)

    @Query("DELETE FROM track_table WHERE trackId = :id")
    suspend fun deleteTrack(id: Long)

    @Query(
        "SELECT * FROM track_table ORDER BY " +
                " trackNumber DESC "
    )
    suspend fun getTracks(): List<TrackEntity>

    @Query("SELECT * FROM track_table WHERE trackId = :trackId")
    suspend fun getTrackById(trackId: Long): TrackEntity
}