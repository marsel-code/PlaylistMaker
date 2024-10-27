package com.example.playlistmaker.media.ui

import android.os.Bundle
import android.util.TypedValue
import android.view.View
import androidx.activity.addCallback
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.media.domain.model.PlayList
import com.example.playlistmaker.media.presentation.view_model.PlayListEditViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class PlayListEditFragment : PlayListDetailsFragment() {

    private var playListId = 0
    override val viewModel by viewModel<PlayListEditViewModel>() { parametersOf(playListId) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        playListId = requireArguments().getInt(GET_PLAY_LIST)!!
        viewModel.getPlayList()

        binding.backButtonPlayer.setTitle(resources.getString(R.string.editPlayListDetails))
        binding.newPlayListButton.text = resources.getString(R.string.savePlayList)

        viewModel.getLiveDateState().observe(viewLifecycleOwner) { playList ->
            showContent(playList.playList)
        }

        binding.backButtonPlayer.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        requireActivity().onBackPressedDispatcher.addCallback(this.viewLifecycleOwner) {
            findNavController().navigateUp()
        }
    }

    fun showContent(playList: PlayList) {
        binding.playlistNameInputText.setText(playList.playListName)
        binding.playlistDescriptionInputText.setText(playList.playListDescription)
        listTracks = playList.tracksIdList
        uriImage = playList.artworkUri
        tracksNumber = playList.numberTracks
        Glide.with(this)
            .load(playList.artworkUri)
            .transform(
                MultiTransformation(
                    CenterCrop(),
                    RoundedCorners(
                        TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_DIP,
                            8F,
                            binding.imagePlaylistDetails.resources.displayMetrics
                        ).toInt()
                    )
                )
            )
            .into(binding.imagePlaylistDetails)
    }

    companion object {
        private const val GET_PLAY_LIST = "GET_PLAY_LIST"
        fun createArgs(playListId: Int): Bundle =
            bundleOf(GET_PLAY_LIST to playListId)
    }
}