package com.example.playlistmaker.media.domain

import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface FavouriteInteractor {

    suspend fun addTrack(track: Track)
    suspend fun deleteTrack(trackId: Long)
    suspend fun getFavouriteTracks(): Flow<List<Track>>
    suspend fun getFavouriteTracksId(track: Track): Flow<Track>
}