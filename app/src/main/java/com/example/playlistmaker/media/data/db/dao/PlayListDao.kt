package com.example.playlistmaker.media.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.playlistmaker.media.data.db.entity.PlayListEntity

@Dao
interface PlayListDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlayList(playList: PlayListEntity)

    @Update(entity = PlayListEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun updatePlayList(playList: PlayListEntity)


    @Query("DELETE FROM play_list_table WHERE playListId = :playListId")
    suspend fun deletePlayList(playListId: Long)


    @Query("SELECT * FROM play_list_table")
    suspend fun getListPlayList(): List<PlayListEntity>


    @Query("SELECT * FROM play_list_table WHERE playListId = :playListId")
    suspend fun getPlayList(playListId: Int): PlayListEntity


}