package com.example.playlistmaker.player.ui

import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.IntentCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityPlayerBinding
import com.example.playlistmaker.player.presentation.state.PlayerScreenState
import com.example.playlistmaker.player.presentation.view_model.PlayerViewModel
import com.example.playlistmaker.search.presentation.model.SearchTrack
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class PlayerActivity : AppCompatActivity() {

    companion object {
        private const val GET_TRACK_PLAYER = "GET_TRACK_PLAYER"
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
    private lateinit var binding: ActivityPlayerBinding
    private lateinit var track: SearchTrack

    private val viewModel by viewModel<PlayerViewModel> { parametersOf(track) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        track = IntentCompat.getParcelableExtra(intent, GET_TRACK_PLAYER, SearchTrack::class.java)!!

        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.playerActivity)) { v, insets ->
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

        backButton.setOnClickListener {
            finish()
        }

        playerButton.setOnClickListener {
            viewModel.onPlayButtonClicked()
        }

        viewModel.getScreenStateLiveData().observe(this) {
            setScreenStateTrack(it)
        }

        viewModel.getPlayerStateLiveData().observe(this) {
            setPlayerStateTrack(it.isPlayButtonEnabled)
            currentTrackTime.text = it.progress
        }
    }

    private fun setPlayerStateTrack(playerState: Boolean) {
        if (playerState) binding.buttonPlay.setImageResource(R.drawable.pause_button) else binding.buttonPlay.setImageResource(
            R.drawable.button_play
        )
    }

    private fun setScreenStateTrack(screenState: PlayerScreenState.Content) {
        trackNamePlayer.text = screenState.trackModel.trackName
        trackArtistPlayer.text = screenState.trackModel.artistName
        trackTimePlayer.text = screenState.trackModel.trackTimeMillis
        headingTrackAlbum.isVisible =
            !screenState.trackModel.collectionName.isNullOrEmpty()
        trackAlbumPlayer.text = screenState.trackModel.collectionName
        trackYearPlayer.text = screenState.trackModel.releaseDate
        trackGenrePlayer.text = screenState.trackModel.primaryGenreName
        trackCountryPlayer.text = screenState.trackModel.country
        urlTrackPreview = screenState.trackModel.previewUrl.toString()

        Glide.with(applicationContext)
            .load(screenState.trackModel.artworkUrl512)
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
        playerButton.isEnabled = true
    }

    override fun onPause() {
        super.onPause()
        viewModel.onPause()
        Log.e("Player", "onPause")
    }
}