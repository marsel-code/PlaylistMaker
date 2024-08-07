package com.example.playlistmaker.search.presentation.mapper

import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.search.presentation.model.SearchTrack

object SearchTrackMapper {
    fun mapSearchTrack(track: Track): SearchTrack {
        return SearchTrack(
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
            artworkUrl512 = track.artworkUrl100?.replaceAfterLast('/', "512x512bb.jpg")
        )
    }

    fun mapTrack(track: SearchTrack): Track {
        return Track(
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
        )
    }
}