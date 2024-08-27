package com.example.playlistmaker.media.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.playlistmaker.databinding.FragmentFavouritesBinding
import com.example.playlistmaker.databinding.FragmentPlaylistBinding
import com.example.playlistmaker.media.presentation.state.FavouritesSate
import com.example.playlistmaker.media.presentation.state.PlayListState
import com.example.playlistmaker.media.presentation.view_model.FavouritesViewModel
import com.example.playlistmaker.media.presentation.view_model.PlaylistViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlayListFragment : Fragment() {

    companion object {
        fun newInstance() = PlayListFragment().apply {
            arguments = Bundle().apply {
            }
        }
    }

    private var _binding: FragmentFavouritesBinding? = null
    private val binding
        get() = _binding!!
    private val viewModel by viewModel<FavouritesViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFavouritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getLiveDateState().observe(viewLifecycleOwner) {
            when (it) {
                is FavouritesSate.Empty -> showEmpty(it.message, it.image)
                is FavouritesSate.Error -> showError(it.errorMessage, it.errorImage)

            }
        }
    }

    private fun showError(errorMessage: String, image: Int) {
        binding.placeholderMessage.text = errorMessage
        binding.placeholderImage.setImageResource(image)
    }

    private fun showEmpty(emptyMessage: String, image: Int) {
        binding.placeholderMessage.text = emptyMessage
        binding.placeholderImage.setImageResource(image)
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}


