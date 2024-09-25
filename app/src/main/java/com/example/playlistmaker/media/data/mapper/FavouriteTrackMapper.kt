package com.example.playlistmaker.media.data.mapper

import com.example.playlistmaker.media.data.db.entity.TrackEntity
import com.example.playlistmaker.search.data.dto.TrackDto
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.search.presentation.model.SearchTrack

class FavouriteTrackMapper {

    fun map(track: TrackEntity): Track {
        return Track(
            trackId = track.trackId,
            trackName = track.trackName,
            artistName = track.trackName,
            trackTimeMillis = track.trackTimeMillis.toString(),
            artworkUrl100 = track.artworkUrl100,
            collectionName = track.collectionName,
            releaseDate = track.releaseDate,
            primaryGenreName = track.primaryGenreName,
            country = track.country,
            previewUrl = track.previewUrl,
        )
    }

    fun map(track: Track): TrackEntity {
        return TrackEntity(
            trackNumber = 0,
            trackId = track.trackId,
            trackName = track.trackName,
            artistName = track.trackName,
            trackTimeMillis = track.trackTimeMillis,
            artworkUrl100 = track.artworkUrl100,
            collectionName = track.collectionName,
            releaseDate = track.releaseDate,
            primaryGenreName = track.primaryGenreName,
            country = track.country,
            previewUrl = track.previewUrl,
            artworkUrl512 = track.artworkUrl100?.replaceAfterLast('/', "512x512bb.jpg"),
        )
    }

}