package com.example.playlistmaker.media.ui

import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlayListInfoBinding
import com.example.playlistmaker.media.domain.model.PlayList
import com.example.playlistmaker.media.presentation.state.PlayListInfoState
import com.example.playlistmaker.media.presentation.view_model.PlayListInfoViewModel
import com.example.playlistmaker.player.ui.PlayerFragment
import com.example.playlistmaker.search.presentation.model.SearchTrack
import com.example.playlistmaker.util.debounce
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlayListInfoFragment : Fragment() {

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 300L
        private const val GET_PLAY_LIST = "GET_PLAY_LIST"

        fun createArgs(playListId: Int): Bundle =
            bundleOf(GET_PLAY_LIST to playListId)
    }

    private var _binding: FragmentPlayListInfoBinding? = null
    private val binding
        get() = _binding!!
    private var adapter: PlayListInfoAdapter? = null
    private lateinit var onClickDebounce: (SearchTrack) -> Unit
    private val viewModel by viewModel<PlayListInfoViewModel>()
    private var playListId: Int = 0
    private lateinit var playList: PlayList
    private lateinit var bottomSheetBehaviorMenu: BottomSheetBehavior<ConstraintLayout>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPlayListInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        playListId = requireArguments().getInt(GET_PLAY_LIST)!!

        viewModel.getPlayList(playListId)

        val bottomSheetBehaviorTrackList = BottomSheetBehavior.from(binding.bottomSheetTrackList)
        bottomSheetBehaviorTrackList.state = BottomSheetBehavior.STATE_COLLAPSED

        bottomSheetBehaviorTrackList.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                    }

                    else -> {
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
            }
        })

        bottomSheetBehaviorMenu = BottomSheetBehavior.from(binding.bottomSheetMenu)
        bottomSheetBehaviorMenu.state = BottomSheetBehavior.STATE_HIDDEN

        bottomSheetBehaviorMenu.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {

            override fun onStateChanged(bottomSheet: View, newState: Int) {

                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        binding.overlay.isVisible = false
                        binding.bottomSheetTrackList.isVisible = true
                    }

                    else -> {
                        binding.overlay.isVisible = true
                        binding.bottomSheetTrackList.isVisible = false
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
            }
        })


        viewModel.getLiveDateState().observe(viewLifecycleOwner) {
            when (it) {
                is PlayListInfoState.Content -> showContent(it.playList)
                is PlayListInfoState.Empty -> findNavController().navigateUp()
            }
        }

        viewModel.getLiveDateStateBottomSheet().observe(viewLifecycleOwner) { content ->
            showBottomSheetTrack(content.tracksList, content.sumTrackTime)
            showBottomSheetMenu(content.playList)
        }

        viewModel.observeShowToast().observe(viewLifecycleOwner) { toast ->
            showToast(toast)
        }

        binding.menuButtonInfo.setOnClickListener {
            bottomSheetBehaviorMenu.state = BottomSheetBehavior.STATE_COLLAPSED

        }

        binding.backButton.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.shareButtonInfo.setOnClickListener {
            viewModel.sharePlayList()
        }

        binding.menuShare.setOnClickListener {
            viewModel.sharePlayList()
        }

        binding.menuDelete.setOnClickListener {
            dialogDeletePlayList(playList)
        }

        binding.menuEdit.setOnClickListener {
            findNavController().navigate(
                R.id.action_playListInfoFragment_to_playListEditFragment,
                PlayListEditFragment.createArgs(playListId)
            )
        }

        onClickDebounce = debounce<SearchTrack>(
            CLICK_DEBOUNCE_DELAY,
            viewLifecycleOwner.lifecycleScope,
            false,
        ) { track ->
            selectTrack(track)
        }

        adapter = PlayListInfoAdapter({ track -> onClickDebounce(track) },
            { track -> longSelectTrack(track) })

        binding.recyclerViewTrackList.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
    }

    private fun selectTrack(track: SearchTrack) {
        findNavController().navigate(
            R.id.action_playListInfoFragment_to_playerFragment,
            PlayerFragment.createArgs(track)
        )
    }

    private fun longSelectTrack(track: SearchTrack): Boolean {
        dialogDeleteTrack(track, playListId)
        return true
    }

    private fun dialogDeleteTrack(track: SearchTrack, playListId: Int) {
        MaterialAlertDialogBuilder(requireContext(), R.style.dialogTheme)
            .setMessage(resources.getString(R.string.delete_track))
            .setNegativeButton(resources.getString(R.string.no)) { dialog, which ->

            }
            .setPositiveButton(resources.getString(R.string.yes)) { dialog, which ->
                viewModel.deleteTrack(track, playListId)
            }
            .show()
    }

    private fun dialogDeletePlayList(playList: PlayList) {
        bottomSheetBehaviorMenu.state = BottomSheetBehavior.STATE_HIDDEN
        MaterialAlertDialogBuilder(requireContext(), R.style.dialogTheme)
            .setMessage(getString(R.string.delete_play_list, playList.playListName))
            .setNegativeButton(resources.getString(R.string.no)) { dialog, which ->
            }
            .setPositiveButton(resources.getString(R.string.yes)) { dialog, which ->
                viewModel.deletePlayList()
            }
            .show()
    }

    private fun showContent(playList: PlayList) {
        this.playList = playList
        binding.playListName.text = playList.playListName
        binding.playlistDescription.text = playList.playListDescription
        Glide.with(requireContext())
            .load(playList.artworkUri)
            .placeholder(R.drawable.no_reply)
            .centerCrop()
            .into(binding.imagePlayList)
        viewModel.getTracks(playList)
        binding.playlistTracksNumber.text = requireContext().resources.getQuantityString(
            R.plurals.plurals_track,
            playList.numberTracks.toInt(),
            playList.numberTracks.toInt()
        )
        binding.playlistDescription.isVisible = playList.playListDescription.isNotEmpty()
        binding.root.post {
            val heightConstraint = binding.constraint.height
            val heightParent = binding.root.height
            Log.d("Height", "$heightParent $heightConstraint")
            val behavior: BottomSheetBehavior<*> =
                BottomSheetBehavior.from<View>(binding.bottomSheetTrackList)
            behavior.peekHeight = heightParent - heightConstraint
            binding.root.requestLayout()
        }
    }

    private fun showBottomSheetTrack(listSearchTrack: List<SearchTrack>, sumTrackTime: Int) {
        binding.recyclerViewTrackList.adapter = adapter
        adapter?.tracksAdapter?.clear()
        adapter?.tracksAdapter?.addAll(listSearchTrack)
        adapter?.notifyDataSetChanged()
        binding.playlistTrackTime.text = requireContext().resources.getQuantityString(
            R.plurals.plurals_time,
            sumTrackTime,
            sumTrackTime
        )
    }

    private fun showBottomSheetMenu(playList: PlayList) {
        Glide.with(requireContext())
            .load(playList.artworkUri)
            .placeholder(R.drawable.no_reply)
            .centerCrop()
            .transform(
                MultiTransformation(
                    CenterCrop(),
                    RoundedCorners(
                        TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_DIP,
                            8F,
                            binding.imageMenu.resources.displayMetrics
                        ).toInt()
                    )
                )
            )
            .into(binding.imageMenu)

        binding.menuPlayListName.text = playList.playListName
        binding.menuTracksNumber.text = requireContext().resources.getQuantityString(
            R.plurals.plurals_track,
            playList.numberTracks.toInt(),
            playList.numberTracks.toInt()
        )
    }

    private fun showToast(text: String) {
        Toast.makeText(
            requireContext(),
            text,
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun onDestroyView() {
        _binding = null
        adapter = null
        super.onDestroyView()
    }
}