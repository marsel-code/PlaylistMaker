package com.example.playlistmaker.media.data.db

import com.example.playlistmaker.media.data.db.entity.TrackEntity
import com.example.playlistmaker.media.data.mapper.FavouriteTrackMapper
import com.example.playlistmaker.media.domain.db.FavouriteRepository
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FavouriteRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val mapper: FavouriteTrackMapper,
): FavouriteRepository {
    override suspend fun addTrack(track: Track){
       appDatabase.trackDao().insertTrack(mapper.map(track))
    }

    override suspend fun deleteTrack(trackId: Long) {
        appDatabase.trackDao().deleteTrack(trackId)
    }

    override suspend fun getFavouriteTracks(): Flow<List<Track>>  = flow {
        val tracks = appDatabase.trackDao().getTracks().map { it -> it  }
        emit(convertFromTrackEntity(tracks))
    }

    override suspend fun getFavouriteTracksId(track: Track): Flow<Track> = flow{
        val tracksId = appDatabase.trackDao().getTrackById(track.trackId)
        if (tracksId != null)    emit(convertFromTrack(tracksId))
    }

    private fun convertFromTrackEntity(tracks: List<TrackEntity>): List<Track> {
        return tracks.map { track -> mapper.map(track) }
    }

    private fun convertFromTrack(tracks: TrackEntity): Track {
        return mapper.map(tracks)
    }
}