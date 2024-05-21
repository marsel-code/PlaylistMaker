package com.example.playlistmaker

import android.app.Activity
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.Locale

class AudioPlayerActivity() : AppCompatActivity() {

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
    private val dateFormat by lazy { SimpleDateFormat("mm:ss", Locale.getDefault()) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_audio_player)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.audioPlayer)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        backButton = findViewById(R.id.backButtonPlayer)
        imageTrackPlayer = findViewById(R.id.imageTrackPlayer)
        trackNamePlayer = findViewById(R.id.trackNamePlayer)
        currentTrackTime = findViewById(R.id.currentTrackTime)
        trackArtistPlayer = findViewById(R.id.trackArtistPlayer)
        trackTimePlayer = findViewById(R.id.trackTimePlayer)
        headingTrackAlbum = findViewById(R.id.headingTrackAlbum)
        trackAlbumPlayer = findViewById(R.id.trackAlbumPlayer)
        trackYearPlayer = findViewById(R.id.trackYearPlayer)
        trackGenrePlayer = findViewById(R.id.trackGenrePlayer)
        trackCountryPlayer = findViewById(R.id.trackCountryPlayer)

        val profileName = intent.getStringExtra("AudioPlayerTrack")
        val trackPlayer = Gson().fromJson(profileName, Track::class.java)
        playerAdapter(trackPlayer)

        backButton.setOnClickListener {
            finish()
        }
    }

    private fun playerAdapter(track: Track) {
        trackNamePlayer.text = track.trackName
        currentTrackTime.text = "0:00"
        trackArtistPlayer.text = track.artistName
        trackTimePlayer.text = dateFormat.format(track.trackTimeMillis)
        if (track.collectionName.isNullOrEmpty()) {
            headingTrackAlbum.isVisible = false
        }
        trackAlbumPlayer.text = track.collectionName
        trackYearPlayer.text = track.releaseDate
        trackGenrePlayer.text = track.primaryGenreName
        trackCountryPlayer.text = track.country

        Glide.with(applicationContext)
            .load(track.artworkUrl100.replaceAfterLast('/', "512x512bb.jpg"))
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
}