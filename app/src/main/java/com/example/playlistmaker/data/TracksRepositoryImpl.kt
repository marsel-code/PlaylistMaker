package com.example.playlistmaker.data

import com.example.playlistmaker.data.dto.TracksSearchRequest
import com.example.playlistmaker.data.dto.iTunesTrackResponse
import com.example.playlistmaker.domain.api.TracksRepository
import com.example.playlistmaker.domain.models.Track

class TracksRepositoryImpl(private val networkClient: NetworkClient) : TracksRepository {

    override fun searchTracks(request: String): List<Track> {
        val response = networkClient.doRequest(TracksSearchRequest(request))
        if (response.resultCode == 200) {
            return (response as iTunesTrackResponse).results.map {
                Track(
                    it.trackId,
                    it.trackName,
                    it.primaryGenreName,
                    it.trackTimeMillis,
                    it.previewUrl,
                    it.artistName,
                    it.releaseDate,
                    it.collectionName,
                    it.artworkUrl100,
                    it.country
                )
            }
        } else {
            return emptyList()
        }
    }

}