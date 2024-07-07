package com.example.playlistmaker.ui.tracks

import android.os.Build
import android.os.Build.VERSION.SDK_INT
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.TypedValue
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.databinding.ActivityAudioPlayerBinding
import com.example.playlistmaker.domain.models.PlayerState
import com.example.playlistmaker.domain.models.Track

class AudioPlayerActivity() : AppCompatActivity() {
    companion object {
        private const val GET_TRACK_PLAYER = "GET_TRACK_PLAYER"
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
    private var mediaPlayer = Creator.providePlayerInteractor()

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

        val profileName: Track? =
            when {
                SDK_INT >= 33 -> intent.getParcelableExtra(GET_TRACK_PLAYER, Track::class.java)
                else -> @Suppress("DEPRECATION") intent.getParcelableExtra(GET_TRACK_PLAYER) as? Track
            }

        if (profileName != null) {
            try {
                playerAdapter(profileName)
            } catch (e: Exception) {
                Toast.makeText(this, "Отсутствует аудио дорожка", Toast.LENGTH_SHORT).show()
            }
        }

        backButton.setOnClickListener {
            finish()
        }

        playerButton.setOnClickListener {
            mediaPlayer.playbackControl()
            if (mediaPlayer.playerState() == PlayerState.STATE_PLAYING) {
                playerButton.setImageResource(R.drawable.pause_button)
                trackTimeUpdate()
            } else {
                playerButton.setImageResource(R.drawable.button_play)
                handler.removeCallbacksAndMessages(null)
            }
        }
    }

    private fun playerAdapter(track: Track) {
        trackNamePlayer.text = track.trackName
        currentTrackTime.text = getString(R.string.currentTrackTime)
        trackArtistPlayer.text = track.artistName
        trackTimePlayer.text = track.trackTimeMillis
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
        track.previewUrl?.let { mediaPlayer.preparePlayer(it) }
        playerButton.isEnabled = true
    }

    override fun onPause() {
        super.onPause()
        mediaPlayer.playerOnPause()
        playerButton.setImageResource(R.drawable.button_play)
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.playerOnDestroy()
        handler.removeCallbacksAndMessages(null)
    }

    fun trackTimeUpdate() {
        handler.postDelayed(
            object : Runnable {
                override fun run() {
                    currentTrackTime.text = mediaPlayer.playerGetCurrentPosition()
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











