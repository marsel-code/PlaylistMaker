package com.example.playlistmaker.player.data.impl

import android.content.Context
import android.media.MediaPlayer
import com.example.playlistmaker.R
import com.example.playlistmaker.player.domain.PlayerRepository
import com.example.playlistmaker.player.presentation.state.PlayerState
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerRepositoryImpl(context: Context) : PlayerRepository {

    private val startProgress = context.getString(R.string.startProgress)
    private val dateFormat by lazy { SimpleDateFormat("mm:ss", Locale.getDefault()) }
    private var mediaPlayer: MediaPlayer = MediaPlayer()
    private var playerState: PlayerState = PlayerState.Default(startProgress)

    override fun initMediaPlayer(urlTrackPreview: String) {
        mediaPlayer.setDataSource(urlTrackPreview)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            playerState = PlayerState.Prepared(startProgress)
        }
        mediaPlayer.setOnCompletionListener {
            playerState = PlayerState.Prepared(startProgress)
        }
    }

    override fun startPlayer() {
        mediaPlayer.start()
        if (mediaPlayer.isPlaying) playerState =
            PlayerState.Playing(dateFormat.format(mediaPlayer.currentPosition))
    }

    override fun pausePlayer() {
        mediaPlayer.pause()
        playerState = PlayerState.Paused(dateFormat.format(mediaPlayer.currentPosition))
    }

    override fun onPlayButtonClicked() {
        when (playerState) {
            is PlayerState.Playing -> {
                pausePlayer()
            }

            is PlayerState.Prepared, is PlayerState.Paused -> {
                startPlayer()
            }

            else -> {}
        }
    }

    override fun releasePlayer() {
        mediaPlayer.stop()
        mediaPlayer.release()
        playerState = PlayerState.Default(startProgress)
    }

    override fun playerState(): PlayerState {
        when (playerState) {
            is PlayerState.Playing -> {
                return PlayerState.Playing(getCurrentPosition())
            }

            is PlayerState.Prepared -> {
                return PlayerState.Prepared(startProgress)
            }

            is PlayerState.Paused -> {
                return PlayerState.Paused(getCurrentPosition())
            }

            is PlayerState.Default -> {
                return PlayerState.Default(startProgress)
            }
        }
    }

    override fun getCurrentPosition(): String {
        return dateFormat.format(mediaPlayer.currentPosition)
    }
}