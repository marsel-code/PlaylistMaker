package com.example.playlistmaker.media.data.mapper

import com.example.playlistmaker.media.data.db.entity.PlayListEntity
import com.example.playlistmaker.media.domain.model.PlayList


object PlayListMapper {
    fun map(playListEntity: PlayListEntity):
            PlayList {
        return PlayList(
            playListId = playListEntity.playListId,
            playListName = playListEntity.playListName,
            playListDescription = playListEntity.playListDescription,
            artworkUrl = playListEntity.artworkUrl,
            trackList = playListEntity.trackList,
            numberTracks = playListEntity.numberTracks
        )
    }

    fun map(playList: PlayList): PlayListEntity {
        return PlayListEntity(
            playListId = playList.playListId,
            playListName = playList.playListName,
            playListDescription = playList.playListDescription,
            artworkUrl = playList.artworkUrl,
            trackList = playList.trackList,
            numberTracks = playList.numberTracks
        )
    }

}