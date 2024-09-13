package com.example.playlistmaker.search.domain.impl

import com.example.playlistmaker.search.domain.TracksInteractor
import com.example.playlistmaker.search.domain.TracksRepository
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.concurrent.Executors

class TracksInteractorImpl(private val repository: TracksRepository) : TracksInteractor {

    override fun searchTracks(request: String) : Flow<Pair<List<Track>?, String?>> {
        return repository.searchTracks(request).map { result ->

            when (result) {
                is Resource.Success -> {
                    Pair(result.data, null)
                }

                is Resource.Error -> {
                    Pair(null, result.message)
                }
            }
        }
    }
}