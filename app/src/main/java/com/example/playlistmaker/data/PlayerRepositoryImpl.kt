package com.example.playlistmaker.data

import android.media.MediaPlayer
import com.example.playlistmaker.domain.api.PlayerRepository
import com.example.playlistmaker.domain.models.PlayerState
import java.text.SimpleDateFormat
import java.util.Locale


class PlayerRepositoryImpl() : PlayerRepository {

//    companion object {
//        private const val STATE_DEFAULT = 0
//        private const val STATE_PREPARED = 1
//        private const val STATE_PLAYING = 2
//        private const val STATE_PAUSED = 3
//        private const val TRACK_TIME_DELAY = 300L
//    }

    private var playerState = PlayerState.STATE_DEFAULT

    private var mediaPlayer = MediaPlayer()

    private val timeFormat by lazy { SimpleDateFormat("mm:ss", Locale.getDefault()) }

    override fun preparePlayer(urlTrackPreview: String) {
        mediaPlayer.setDataSource(urlTrackPreview)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            playerState = PlayerState.STATE_PREPARED
        }
        mediaPlayer.setOnCompletionListener {
            playerState = PlayerState.STATE_PREPARED
        }
    }

    override fun startPlayer() {
        mediaPlayer.start()
        playerState = PlayerState.STATE_PLAYING
    }

    override fun pausePlayer() {
        mediaPlayer.pause()
        playerState = PlayerState.STATE_PAUSED
    }

    override fun playbackControl() {
        when (playerState) {
            PlayerState.STATE_PLAYING -> {
                pausePlayer()

            }
            PlayerState.STATE_PREPARED, PlayerState.STATE_PAUSED -> {
                startPlayer()

            }

            PlayerState.STATE_DEFAULT -> TODO()
        }
    }

    override fun playerState(): PlayerState {
        return playerState
    }

    override fun playerGetCurrentPosition():String{
        return timeFormat.format(mediaPlayer.getCurrentPosition())
    }


    override fun playerOnPause(){
        pausePlayer()
    }


    override fun playerOnDestroy(){
        mediaPlayer.release()
    }











}