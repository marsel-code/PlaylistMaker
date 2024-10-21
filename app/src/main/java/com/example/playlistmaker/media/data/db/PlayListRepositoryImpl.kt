package com.example.playlistmaker.media.data.db

import com.example.playlistmaker.media.data.db.entity.PlayListEntity
import com.example.playlistmaker.media.data.mapper.PlayListMapper
import com.example.playlistmaker.media.domain.PlayListRepository
import com.example.playlistmaker.media.domain.model.PlayList
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PlayListRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val mapper: PlayListMapper
) : PlayListRepository {

    override suspend fun addPlayList(playList: PlayList) {
        appDatabase.playListDao().insertPlayList(mapper.map(playList))
    }

    override suspend fun saveTack(track: Track) {
       appDatabase.saveTrackDao().saveTrack(mapper.map(track))
    }

    override suspend fun deletePlayList(playListId: Int) {
        appDatabase.playListDao().getPlayList(playListId)
    }

    override suspend fun updatePlayList(playList: PlayList) {
        appDatabase.playListDao().updatePlayList(mapper.map(playList))
    }

    override suspend fun getListPlayList(): Flow<List<PlayList>> = flow{
        val playList = appDatabase.playListDao().getListPlayList().map{it->it}
        emit (convertFromPlayList(playList))
    }

    override suspend fun getPlayList(playListId: Int): Flow<PlayList> =flow {
        val playList = appDatabase.playListDao().getPlayList(playListId)
        emit (mapper.map(playList))
    }

    private fun convertFromPlayList(playListEntity:List<PlayListEntity>): List<PlayList>{
        return playListEntity.map { playList-> mapper.map(playList) }
    }



}

