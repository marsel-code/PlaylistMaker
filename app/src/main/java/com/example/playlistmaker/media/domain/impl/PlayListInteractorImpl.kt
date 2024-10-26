package com.example.playlistmaker.media.domain.impl


import com.example.playlistmaker.media.domain.PlayListInteractor
import com.example.playlistmaker.media.domain.PlayListRepository
import com.example.playlistmaker.media.domain.model.PlayList
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

class PlayListInteractorImpl(private val playListRepository: PlayListRepository) :
    PlayListInteractor {

    override suspend fun addPlayList(playList: PlayList) {
        return playListRepository.addPlayList(playList)
    }

    override suspend fun saveTack(track: Track) {
        return playListRepository.saveTack(track)
    }

    override suspend fun deleteTack(trackId: Long) {
        return playListRepository.deleteTack(trackId)
    }

    override suspend fun deletePlayList(playList: PlayList) {
        return playListRepository.deletePlayList(playList)
    }

    override suspend fun updatePlayList(playList: PlayList) {
        return playListRepository.updatePlayList(playList)
    }

    override suspend fun getListPlayList(): Flow<List<PlayList>> {
        return playListRepository.getListPlayList()
    }

    override suspend fun getPlayList(playListId: Int): Flow<PlayList> {
        return playListRepository.getPlayList(playListId)
    }

    override suspend fun getListTrack(listTracks: List<Long>): Flow<List<Track>> {
        return playListRepository.getListTrack(listTracks)
    }

    override suspend fun deleteTrackPlayList(playList: PlayList) {
        return playListRepository.deleteTrackPlayList(playList)
    }

}