package com.example.playlistmaker.media.domain.db


import com.example.playlistmaker.media.domain.model.PlayList
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface PlayListInteractor {

    suspend fun addPlayList(playList: PlayList)
    suspend fun saveTack(track: Track)
    suspend fun deletePlayList(playListId: Int)
    suspend fun updatePlayList(playList: PlayList)
    suspend fun getListPlayList(): Flow<List<PlayList>>
    suspend fun getPlayList(playListId: Int): Flow<PlayList>
}