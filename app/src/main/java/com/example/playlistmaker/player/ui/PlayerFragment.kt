package com.example.playlistmaker.player.ui

import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlayerBinding
import com.example.playlistmaker.media.domain.model.PlayList
import com.example.playlistmaker.player.presentation.state.PlayerBottomSheetState
import com.example.playlistmaker.player.presentation.state.PlayerScreenState
import com.example.playlistmaker.player.presentation.view_model.PlayerViewModel
import com.example.playlistmaker.search.presentation.model.SearchTrack
import com.example.playlistmaker.util.debounce
import com.google.android.material.bottomsheet.BottomSheetBehavior
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class PlayerFragment : Fragment() {

    companion object {
        private const val GET_TRACK_PLAYER = "GET_TRACK_PLAYER"
        private const val CLICK_DEBOUNCE_DELAY = 300L

        fun createArgs(searchTrack: SearchTrack): Bundle =
            bundleOf(GET_TRACK_PLAYER to searchTrack)
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
    private lateinit var likeButton: ImageButton
    private lateinit var newPlayList: Button
    private lateinit var addPlayListButton: ImageButton
    private lateinit var bottomSheetContainer: LinearLayout
    private lateinit var urlTrackPreview: String
    private lateinit var binding: FragmentPlayerBinding
    private lateinit var track: SearchTrack
    private var adapter: PlayerPlayListAdapter? = null
    private lateinit var onClickDebounce: (PlayList) -> Unit

    private val viewModel by viewModel<PlayerViewModel> { parametersOf(track) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPlayerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        track = requireArguments().getParcelable<SearchTrack>(GET_TRACK_PLAYER)!!

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
        likeButton = binding.buttonLike
        addPlayListButton = binding.buttonPlayList
        bottomSheetContainer = binding.standardBottomSheet
        newPlayList = binding.newPlayList

        val bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetContainer)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN

        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {

            override fun onStateChanged(bottomSheet: View, newState: Int) {

                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        binding.overlay.isVisible = false
                    }

                    else -> {
                        binding.overlay.isVisible = true
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {}
        })

        onClickDebounce = debounce<PlayList>(
            CLICK_DEBOUNCE_DELAY,
            viewLifecycleOwner.lifecycleScope,
            false,
        ) { playList ->
            selectPlayList(playList)
        }

        adapter = PlayerPlayListAdapter { playList ->
            onClickDebounce(playList)
        }

        binding.recyclerViewPlayList.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)


        backButton.setOnClickListener {
            findNavController().navigateUp()
        }

        addPlayListButton.setOnClickListener {
            viewModel.playListUpdate()
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }

        playerButton.setOnClickListener {
            viewModel.onPlayButtonClicked()
        }

        likeButton.setOnClickListener {
            viewModel.onFavoriteClicked()
        }

        newPlayList.setOnClickListener {
            findNavController().navigate(
                R.id.action_playerFragment_to_playListDetalisFragment,
            )
        }


        viewModel.checkingTrackFavourites()

        viewModel.getScreenStateLiveData().observe(viewLifecycleOwner) {
            setScreenStateTrack(it)
        }

        viewModel.getPlayerStateLiveData().observe(viewLifecycleOwner) {
            setPlayerStateTrack(it.isPlayButtonEnabled)
            currentTrackTime.text = it.progress
        }

        viewModel.getPlayerBottomSheetLiveData().observe(viewLifecycleOwner) {

            when (it) {

                is PlayerBottomSheetState.Content -> showBottomSheet(it.listPlayList)
                is PlayerBottomSheetState.Nothing -> bottomSheetBehavior.state =
                    BottomSheetBehavior.STATE_HIDDEN
            }


        }

        viewModel.observeShowToast().observe(viewLifecycleOwner) { toast ->
            showToast(toast)
        }


    }

    private fun selectPlayList(playList: PlayList) {
        viewModel.checkingTrackPlayList(playList, track)
    }


    private fun showToast(text: String) {
        Toast.makeText(
            requireContext(),
            text,
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun showBottomSheet(listPlayList: List<PlayList>) {
        binding.recyclerViewPlayList.adapter = adapter
        adapter?.listPlayList?.clear()
        adapter?.listPlayList?.addAll(listPlayList)
        adapter?.notifyDataSetChanged()
    }

    private fun setPlayerStateTrack(playerState: Boolean) {
        if (playerState) binding.buttonPlay.setImageResource(R.drawable.pause_button) else binding.buttonPlay.setImageResource(
            R.drawable.button_play
        )
    }

    private fun setButtonLikeState(buttonLikeState: Boolean) {
        if (buttonLikeState) binding.buttonLike.setImageResource(R.drawable.button_like_active) else binding.buttonLike.setImageResource(
            R.drawable.button_like
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
        setButtonLikeState(screenState.trackModel.isFavorite)

        Glide.with(requireContext())
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