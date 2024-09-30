package com.example.playlistmaker.media.domain.impl


import com.example.playlistmaker.media.data.db.entity.TrackEntity
import com.example.playlistmaker.media.domain.db.FavouriteInteractor
import com.example.playlistmaker.media.domain.db.FavouriteRepository
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

class FavouriteInteractorImpl(private val favouriteRepository: FavouriteRepository) :
    FavouriteInteractor {
    override suspend fun addTrack(track: Track) {
        return favouriteRepository.addTrack(track)
    }

    override suspend  fun deleteTrack(trackId: Long) {
        return favouriteRepository.deleteTrack(trackId)
    }

    override suspend fun getFavouriteTracks(): Flow<List<Track>> {
        return favouriteRepository.getFavouriteTracks()
    }

    override suspend fun getFavouriteTracksId(track: Track): Flow<Track> {
        return favouriteRepository.getFavouriteTracksId(track)
    }
}


