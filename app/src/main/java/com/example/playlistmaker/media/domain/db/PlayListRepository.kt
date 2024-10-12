package com.example.playlistmaker.media.domain.db


import com.example.playlistmaker.media.domain.model.PlayList
import kotlinx.coroutines.flow.Flow

interface PlayListRepository {

    suspend fun addPlayList(playList: PlayList)
    suspend fun deletePlayList(playListId: Long)
    suspend fun updatePlayList(playList: PlayList)
    suspend fun getListPlayList(): Flow<List<PlayList>>
    suspend fun getPlayList(playListId: Long): Flow<PlayList>
 }