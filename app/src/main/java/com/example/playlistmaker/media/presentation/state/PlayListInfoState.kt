package com.example.playlistmaker.media.presentation.state

import com.example.playlistmaker.media.domain.model.PlayList
import com.example.playlistmaker.search.presentation.model.SearchTrack

interface PlayListInfoState {

    class Empty() : PlayListInfoState

    data class Content(
        val playList: PlayList
    ) : PlayListInfoState

    data class ContentBottomSheet(
        val playList: PlayList,
        val tracksList: List<SearchTrack>,
        val sumTrackTime: Int
    ) : PlayListInfoState

}