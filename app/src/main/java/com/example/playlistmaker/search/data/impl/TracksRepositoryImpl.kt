package com.example.playlistmaker.search.data.impl

import com.example.playlistmaker.search.data.network.NetworkClient
import com.example.playlistmaker.search.data.dto.TracksSearchRequest
import com.example.playlistmaker.search.data.dto.ITunesTrackResponse
import com.example.playlistmaker.search.domain.TracksRepository
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.util.Resource
import java.text.SimpleDateFormat
import java.util.Locale

class TracksRepositoryImpl(private val networkClient: NetworkClient) : TracksRepository {

    private val timeFormat by lazy { SimpleDateFormat("mm:ss", Locale.getDefault()) }
    private val dateFormat by lazy { SimpleDateFormat("YYYY", Locale.getDefault()) }

    override fun searchTracks(request: String): Resource<List<Track>> {
        val response = networkClient.doRequest(TracksSearchRequest(request))
        return when (response.resultCode) {
            -1 -> {
                Resource.Error("Проверьте подключение к интернету")
            }

            200 -> {
                Resource.Success((response as ITunesTrackResponse).results.map {
                    Track(
                        it.trackId,
                        it.trackName,
                        it.artistName,
                        trackTimeMillis = timeFormat.format(it.trackTimeMillis),
                        it.artworkUrl100,
                        it.collectionName,
                        releaseDate = it.releaseDate?.let {
                            dateFormat.parse(it)?.let { dateFormat.format(it) }
                        },
                        it.primaryGenreName,
                        it.country,
                        it.previewUrl
                    )
                })
            }

            else -> {
                Resource.Error("")
            }
        }
    }
}


