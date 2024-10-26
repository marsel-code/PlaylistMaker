package com.example.playlistmaker.media.data.db


import com.example.playlistmaker.media.data.db.entity.PlayListEntity
import com.example.playlistmaker.media.data.db.entity.SaveTrackEntity
import com.example.playlistmaker.media.data.mapper.FavouriteTrackMapper
import com.example.playlistmaker.media.data.mapper.PlayListMapper
import com.example.playlistmaker.media.domain.PlayListRepository
import com.example.playlistmaker.media.domain.model.PlayList
import com.example.playlistmaker.search.domain.models.Track
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.lang.reflect.Type

class PlayListRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val mapperPlayList: PlayListMapper,
    private val mapperTrack: FavouriteTrackMapper
) : PlayListRepository {

    override suspend fun addPlayList(playList: PlayList) {
        appDatabase.playListDao().insertPlayList(mapperPlayList.map(playList))
    }

    override suspend fun saveTack(track: Track) {
        appDatabase.saveTrackDao().saveTrack(mapperPlayList.map(track))
    }

    override suspend fun deleteTack(trackId: Long) {
        if (checkingBeforeDeletingTrack(trackId)) appDatabase.saveTrackDao().deleteTrack(trackId)
    }

    override suspend fun deletePlayList(playList: PlayList) {
        appDatabase.playListDao().deletePlayList(playList.playListId)
    }

    override suspend fun updatePlayList(playList: PlayList) {
        appDatabase.playListDao().updatePlayList(mapperPlayList.map(playList))
    }

    override suspend fun getListPlayList(): Flow<List<PlayList>> = flow {
        val playList = appDatabase.playListDao().getListPlayList().map { it -> it }
        emit(convertFromPlayList(playList))
    }

    override suspend fun getPlayList(playListId: Int): Flow<PlayList> = flow {
        val playList = appDatabase.playListDao().getPlayList(playListId)
        emit(mapperPlayList.map(playList))
    }

    override suspend fun getListTrack(listTracks: List<Long>): Flow<List<Track>> = flow {
        val saveTracksList =
            appDatabase.saveTrackDao().getTrackListById(listTracks).map { it -> it }
        emit(convertFromSaveTrackEntity(saveTracksList))
    }

    private fun convertFromPlayList(playListEntity: List<PlayListEntity>): List<PlayList> {
        return playListEntity.map { playList -> mapperPlayList.map(playList) }
    }

    private fun convertFromSaveTrackEntity(tracks: List<SaveTrackEntity>): List<Track> {
        return tracks.map { track -> mapperTrack.map(track) }
    }

    private suspend fun checkingBeforeDeletingTrack(trackId: Long): Boolean {
        var trackIdNotFound = true
        getListPlayList().collect { listPlayList ->
            listPlayList.forEach {
                val trackIdList = mapperPlayList.listFromJson(it.tracksIdList).iterator()
                while (trackIdList.hasNext()) {
                    if (trackIdList.next() == trackId) trackIdNotFound = false
                }
            }
        }
        return trackIdNotFound
    }

    override suspend fun deleteTrackPlayList(playList: PlayList) {
        val tracksListInterator = mapperPlayList.listFromJson(playList.tracksIdList).iterator()
        while (tracksListInterator.hasNext()) {
            deleteTack(tracksListInterator.next())
        }
    }
}


