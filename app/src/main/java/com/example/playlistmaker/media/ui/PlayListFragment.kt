package com.example.playlistmaker.media.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlaylistBinding
import com.example.playlistmaker.media.domain.model.PlayList
import com.example.playlistmaker.media.presentation.state.PlayListState
import com.example.playlistmaker.media.presentation.view_model.PlaylistViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlayListFragment : Fragment() {

    companion object {
        fun newInstance() = PlayListFragment().apply {
            arguments = Bundle().apply {
            }
        }
    }

    private lateinit var createPlaylistButton: Button
    private var _binding: FragmentPlaylistBinding? = null
    private val binding
        get() = _binding!!
    private val viewModel by viewModel<PlaylistViewModel>()



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerView.layoutManager = GridLayoutManager(requireContext(),2)


        viewModel.getLiveDateState().observe(viewLifecycleOwner) {
            when (it) {
                is PlayListState.Content -> showContent(it.playList)
                is PlayListState.Empty -> showEmpty(it.message, it.image)
                is PlayListState.Error -> showError(it.errorMessage, it.errorImage)
            }
        }
        createPlaylistButton = binding.newPlayList

        createPlaylistButton.setOnClickListener {
            findNavController().navigate(R.id.action_mediaFragment_to_playListDetalisFragment)
        }
    }

    private fun showContent(playList: List<PlayList>) {
        binding.placeholderMessage.isVisible = false
        binding.placeholderImage.isVisible = false
        binding.recyclerView.isVisible = true
        binding.recyclerView.adapter = PlayListAdapter(playList)
    }


    private fun showError(errorMessage: String, image: Int) {
        binding.placeholderMessage.text = errorMessage
        binding.placeholderImage.setImageResource(image)
    }

    private fun showEmpty(emptyMessage: String, image: Int) {
        binding.placeholderMessage.text = emptyMessage
        binding.placeholderImage.setImageResource(image)
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


