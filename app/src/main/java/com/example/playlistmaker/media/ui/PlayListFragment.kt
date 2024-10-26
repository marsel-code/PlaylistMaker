package com.example.playlistmaker.media.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlaylistBinding
import com.example.playlistmaker.media.domain.model.PlayList
import com.example.playlistmaker.media.presentation.state.PlayListState
import com.example.playlistmaker.media.presentation.view_model.PlaylistViewModel
import com.example.playlistmaker.util.debounce
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlayListFragment : Fragment() {

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 300L
        fun newInstance() = PlayListFragment().apply {
            arguments = Bundle().apply {
            }
        }
    }

    private var _binding: FragmentPlaylistBinding? = null
    private val binding
        get() = _binding!!
    private val viewModel by viewModel<PlaylistViewModel>()
    private lateinit var onClickDebounce: (PlayList) -> Unit
    private var adapter: PlayListAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        onClickDebounce = debounce<PlayList>(
            CLICK_DEBOUNCE_DELAY,
            viewLifecycleOwner.lifecycleScope,
            false,
        ) { playList ->
            selectPlayList(playList)
        }

        adapter = PlayListAdapter { playList ->
            onClickDebounce(playList)
        }


        binding.recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)

        viewModel.getLiveDateState().observe(viewLifecycleOwner) {
            when (it) {
                is PlayListState.Content -> showContent(it.playList)
                is PlayListState.Empty -> showEmpty(it.message, it.image)
                is PlayListState.Error -> showError(it.errorMessage, it.errorImage)
            }
        }

        binding.newPlayList.setOnClickListener {
            findNavController().navigate(
                R.id.action_mediaFragment_to_playListDetalisFragment
            )
        }
    }

    private fun showContent(playList: List<PlayList>) {
        binding.placeholderMessage.isVisible = false
        binding.placeholderImage.isVisible = false
        binding.recyclerView.isVisible = true
        binding.recyclerView.adapter = adapter
        adapter?.listPlayList?.clear()
        adapter?.listPlayList?.addAll(playList)
        adapter?.notifyDataSetChanged()
    }


    private fun showError(errorMessage: String, image: Int) {
        binding.placeholderMessage.text = errorMessage
        binding.placeholderImage.setImageResource(image)
    }

    private fun showEmpty(emptyMessage: String, image: Int) {
        binding.placeholderMessage.isVisible = true
        binding.placeholderImage.isVisible = true
        binding.recyclerView.isVisible = false
        binding.placeholderMessage.text = emptyMessage
        binding.placeholderImage.setImageResource(image)
    }

    private fun selectPlayList(playList: PlayList) {
        findNavController().navigate(
            R.id.action_mediaFragment_to_playListInfoFragment,
            PlayListInfoFragment.createArgs(playList.playListId)
        )
    }

    override fun onResume() {
        viewModel.renderState()
        super.onResume()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}


