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

    override suspend fun saveTack(track: Track){
        return playListRepository.saveTack(track)
    }


    override suspend fun deletePlayList(playListId: Int) {
        return playListRepository.deletePlayList(playListId)
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
}