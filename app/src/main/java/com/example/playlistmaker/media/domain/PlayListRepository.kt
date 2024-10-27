package com.example.playlistmaker.media.domain


import com.example.playlistmaker.media.domain.model.PlayList
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface PlayListRepository {

    suspend fun addPlayList(playList: PlayList)
    suspend fun saveTack(track: Track)
    suspend fun deleteTack(trackId: Long)
    suspend fun deletePlayList(playList: PlayList)
    suspend fun updatePlayList(playList: PlayList)
    suspend fun getListPlayList(): Flow<List<PlayList>>
    suspend fun getPlayList(playListId: Int): Flow<PlayList>
    suspend fun getTrack(trackId: Long): Flow<Track>
    suspend fun getListTrack(listTracks: List<Long>): Flow<List<Track>>
    suspend fun deleteTrackPlayList(playList: PlayList)
}