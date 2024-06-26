package com.example.playlistmaker.data.dto

import com.example.playlistmaker.domain.models.Track

class iTunesTrackResponse(val results: MutableList<Track>): Response()

//class iTunesTrackResponse(val results: MutableList<TrackDto>): Response()