package com.example.playlistmaker.media.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentFavouritesBinding
import com.example.playlistmaker.media.presentation.state.FavouritesSate
import com.example.playlistmaker.media.presentation.view_model.FavouritesViewModel
import com.example.playlistmaker.player.ui.PlayerFragment
import com.example.playlistmaker.search.presentation.model.SearchTrack
import com.example.playlistmaker.search.ui.TrackAdapter
import com.example.playlistmaker.util.debounce
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavouritesFragment : Fragment() {

    companion object {
        fun newInstance() = FavouritesFragment().apply {
            arguments = Bundle().apply {
            }
        }

        private const val CLICK_DEBOUNCE_DELAY = 300L
    }

    private lateinit var recyclerTrack: RecyclerView
    private lateinit var placeholderMessage: TextView
    private lateinit var placeholderImage: ImageView
    private lateinit var progressBar: ProgressBar
    private var adapter: TrackAdapter? = null

    private var _binding: FragmentFavouritesBinding? = null
    private val binding
        get() = _binding!!

    private val viewModel by viewModel<FavouritesViewModel>()

    private lateinit var onClickDebounce: (SearchTrack) -> Unit

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFavouritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerTrack = binding.trackList
        placeholderMessage = binding.placeholderMessage
        placeholderImage = binding.placeholderImage
        progressBar = binding.progressBar

        recyclerTrack.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        onClickDebounce = debounce<SearchTrack>(
            CLICK_DEBOUNCE_DELAY,
            viewLifecycleOwner.lifecycleScope,
            false,
        ) { track ->
            selectTrack(track)
        }

        adapter = TrackAdapter { track ->
            onClickDebounce(track)
        }

        viewModel.getLiveDateState().observe(viewLifecycleOwner) {
            render(it)
        }
    }

    private fun selectTrack(track: SearchTrack) {
        findNavController().navigate(
            R.id.action_mediaFragment_to_playerFragment,
            PlayerFragment.createArgs(track)
        )
    }

    private fun render(state: FavouritesSate) {
        when (state) {
            is FavouritesSate.Content -> showContent(state.tracks)
            is FavouritesSate.Empty -> showEmpty(state.message, state.image)
            is FavouritesSate.Error -> showError(state.errorMessage, state.errorImage)
            is FavouritesSate.Loading -> showLoading()
        }
    }

    private fun showLoading() {
        recyclerTrack.isVisible = false
        progressBar.isVisible = true
        visibilityError(false)
    }

    private fun showContent(tracks: List<SearchTrack>) {
        recyclerTrack.isVisible = false
        visibilityError(false)
        progressBar.isVisible = false
        recyclerTrack.isVisible = true
        recyclerTrack.adapter = adapter
        adapterData(tracks)
    }


    fun visibilityError(s: Boolean) {
        if (s) {
            placeholderMessage.isVisible = true
            placeholderImage.isVisible = true
        } else {
            placeholderMessage.isVisible = false
            placeholderImage.isVisible = false
        }
    }

    private fun showError(errorMessage: String, image: Int) {
        visibilityError(true)
        placeholderMessage.text = errorMessage
        placeholderImage.setImageResource(image)
        recyclerTrack.isVisible = false
        progressBar.isVisible = false
    }

    private fun showEmpty(emptyMessage: String, image: Int) {
        visibilityError(true)
        recyclerTrack.isVisible = false
        placeholderMessage.text = emptyMessage
        placeholderImage.setImageResource(image)
        progressBar.isVisible = false
    }

    fun adapterData(trackListAdapter: List<SearchTrack>) {
        adapter?.tracksAdapter?.clear()
        adapter?.tracksAdapter?.addAll(trackListAdapter)
        adapter?.notifyDataSetChanged()
    }

    override fun onResume() {
        viewModel.renderState()
        super.onResume()
    }

    override fun onDestroyView() {
        _binding = null
        adapter = null
        recyclerTrack.adapter = null
        super.onDestroyView()
    }
}


