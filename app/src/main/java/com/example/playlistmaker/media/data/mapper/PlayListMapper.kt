package com.example.playlistmaker.media.data.mapper

import com.example.playlistmaker.media.data.db.entity.PlayListEntity
import com.example.playlistmaker.media.data.db.entity.SaveTrackEntity
import com.example.playlistmaker.media.domain.model.PlayList
import com.example.playlistmaker.search.domain.models.Track


class PlayListMapper {

    fun map(playListEntity: PlayListEntity):
            PlayList {
        return PlayList(
            playListId = playListEntity.playListId,
            playListName = playListEntity.playListName,
            playListDescription = playListEntity.playListDescription,
            artworkUri = playListEntity.artworkUri,
            numberTracks = playListEntity.numberTracks,
            tracksIdList = playListEntity.tracksIdList

        )
    }

    fun map(playList: PlayList): PlayListEntity {
        return PlayListEntity(
            playListId = playList.playListId,
            playListName = playList.playListName,
            playListDescription = playList.playListDescription,
            artworkUri = playList.artworkUri,
            tracksIdList = playList.tracksIdList,
            numberTracks = playList.numberTracks
        )
    }

    fun map(track: Track): SaveTrackEntity {
        return SaveTrackEntity(
            trackId = track.trackId,
            trackName = track.trackName,
            artistName = track.artistName,
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


}