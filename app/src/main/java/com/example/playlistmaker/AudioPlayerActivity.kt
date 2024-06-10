package com.example.playlistmaker

import android.app.Activity
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.TypedValue
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.databinding.ActivityAudioPlayerBinding
import com.google.gson.Gson
import kotlinx.coroutines.Runnable
import java.text.SimpleDateFormat
import java.util.Locale


class AudioPlayerActivity() : AppCompatActivity() {
    companion object {
        private const val GET_TRACK_PLAYER = "GET_TRACK_PLAYER"
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
        private const val TRACK_TIME_DELAY = 300L
    }

    private lateinit var backButton: Toolbar
    private lateinit var imageTrackPlayer: ImageView
    private lateinit var trackNamePlayer: TextView
    private lateinit var currentTrackTime: TextView
    private lateinit var trackArtistPlayer: TextView
    private lateinit var trackTimePlayer: TextView
    private lateinit var headingTrackAlbum: TextView
    private lateinit var trackAlbumPlayer: TextView
    private lateinit var trackYearPlayer: TextView
    private lateinit var trackGenrePlayer: TextView
    private lateinit var trackCountryPlayer: TextView
    private lateinit var playerButton: ImageButton
    private lateinit var urlTrackPreview: String
    private lateinit var binding: ActivityAudioPlayerBinding
    private val handler = Handler(Looper.getMainLooper())
    private var playerState = STATE_DEFAULT
    private val dateFormat by lazy { SimpleDateFormat("mm:ss", Locale.getDefault()) }
    private var mediaPlayer = MediaPlayer()

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAudioPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.audioPlayer)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        backButton = binding.backButtonPlayer
        imageTrackPlayer = binding.imageTrackPlayer
        trackNamePlayer = binding.trackNamePlayer
        currentTrackTime = binding.currentTrackTime
        trackArtistPlayer = binding.trackArtistPlayer
        trackTimePlayer = binding.trackTimePlayer
        headingTrackAlbum = binding.headingTrackAlbum
        trackAlbumPlayer = binding.trackAlbumPlayer
        trackYearPlayer = binding.trackYearPlayer
        trackGenrePlayer = binding.trackGenrePlayer
        trackCountryPlayer = binding.trackCountryPlayer
        playerButton = binding.buttonPlay

        val profileName = intent.getParcelableExtra(GET_TRACK_PLAYER, Track::class.java)
        if (profileName != null) {
            playerAdapter(profileName)
            preparePlayer()
        }

        backButton.setOnClickListener {
            finish()
        }

        playerButton.setOnClickListener {
            playbackControl()
        }
    }

    private fun playerAdapter(track: Track) {
        trackNamePlayer.text = track.trackName
        currentTrackTime.text = getString(R.string.currentTrackTime)
        trackArtistPlayer.text = track.artistName
        trackTimePlayer.text = dateFormat.format(track.trackTimeMillis)
        headingTrackAlbum.isVisible = !track.collectionName.isNullOrEmpty()
        trackAlbumPlayer.text = track.collectionName
        trackYearPlayer.text = track.releaseDate
        trackGenrePlayer.text = track.primaryGenreName
        trackCountryPlayer.text = track.country
        urlTrackPreview = track.previewUrl.toString()

        Glide.with(applicationContext)
            .load(track.artworkUrl512)
            .placeholder(R.drawable.no_reply)
            .centerCrop()
            .transform(
                RoundedCorners(
                    TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP,
                        8F,
                        imageTrackPlayer.resources.displayMetrics
                    ).toInt()
                )
            )
            .into(imageTrackPlayer)
    }

    private fun preparePlayer() {
        mediaPlayer.setDataSource(urlTrackPreview)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            playerButton.isEnabled = true
            playerState = STATE_PREPARED
        }
        mediaPlayer.setOnCompletionListener {
            playerButton.setImageResource(R.drawable.button_play)
            playerState = STATE_PREPARED
            handler.removeCallbacksAndMessages(null)
            currentTrackTime.text = getString(R.string.currentTrackTime)
        }
    }

    private fun startPlayer() {
        mediaPlayer.start()
        playerButton.setImageResource(R.drawable.pause_button)
        playerState = STATE_PLAYING
        trackTimeUpdate()
    }

    private fun pausePlayer() {
        mediaPlayer.pause()
        playerButton.setImageResource(R.drawable.button_play)
        playerState = STATE_PAUSED
        handler.removeCallbacksAndMessages(null)
    }

    private fun playbackControl() {
        when (playerState) {
            STATE_PLAYING -> {
                pausePlayer()
            }

            STATE_PREPARED, STATE_PAUSED -> {
                startPlayer()
            }
        }
    }

    override fun onPause() {
        super.onPause()
        pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
        handler.removeCallbacksAndMessages(null)
    }

    fun trackTimeUpdate() {
        handler.postDelayed(
            object : Runnable {
                override fun run() {
                    currentTrackTime.text = dateFormat.format(mediaPlayer.getCurrentPosition())
                    handler.postDelayed(
                        this,
                        TRACK_TIME_DELAY,
                    )
                }
            },
            TRACK_TIME_DELAY
        )
    }
}











