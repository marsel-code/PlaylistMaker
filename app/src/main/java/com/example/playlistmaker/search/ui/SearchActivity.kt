package com.example.playlistmaker.search.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.databinding.ActivitySearchBinding
import com.example.playlistmaker.player.ui.PlayerActivity
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.search.presentation.state.SearchState
import com.example.playlistmaker.search.presentation.view_model.SearchViewModel


class SearchActivity : AppCompatActivity() {

    companion object {
        private const val KEY_TEXT = "KEY_TEXT"
        private const val TAG = "SPRINT_9"
        private const val GET_TRACK_PLAYER = "GET_TRACK_PLAYER"
        private const val CLICK_DEBOUNCE_DELAY = 1000L
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }

    private lateinit var binding: ActivitySearchBinding
    private var editTextValue: String? = ""
    private lateinit var backButton: Toolbar
    private lateinit var inputEditText: EditText
    private lateinit var clearButton: ImageView
    private lateinit var recyclerTrack: RecyclerView
    private lateinit var recyclerSearchTrack: RecyclerView
    private lateinit var placeholderMessage: TextView
    private lateinit var placeholderImage: ImageView
    private lateinit var updateButton: Button
    private lateinit var clearSearchListButton: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var searchLayout: LinearLayout

    private lateinit var viewModel: SearchViewModel
    private var isClickAllowed = true
    private val handler = Handler(Looper.getMainLooper())
    private val adapter = TrackAdapter {
        if (clickDebounce()) {
            selectTrack(it)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(
            this,
            SearchViewModel.getViewModelFactory()
        )[SearchViewModel::class.java]

        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        backButton = binding.backMain
        inputEditText = binding.inputEditText
        clearButton = binding.clearIcon
        recyclerTrack = binding.trackList
        recyclerSearchTrack = binding.searchList
        placeholderMessage = binding.placeholderMessage
        placeholderImage = binding.placeholderImage
        updateButton = binding.updateButton
        clearSearchListButton = binding.clearSearchList
        searchLayout = binding.searchLayout
        progressBar = binding.progressBar

        recyclerTrack.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerSearchTrack.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        backButton.setOnClickListener {
            finish()
        }

        clearButton.setOnClickListener {
            inputEditText.setText("")
            recyclerTrack.isVisible = false
            searchLayout.isVisible = false
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(inputEditText.windowToken, 0)
        }

        clearSearchListButton.setOnClickListener {
            viewModel.searchHistoryClear()
            searchLayout.isVisible = false
        }

        updateButton.setOnClickListener {
            editTextValue?.let { it1 -> viewModel.searchDebounce(it1) }
        }

        val simpleTextWatcher = object : TextWatcher {

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                clearButton.visibility = buttonVisibility(s)
                editTextValue = s.toString()
                if (inputEditText.hasFocus() && editTextValue?.isEmpty() == true) {
                    viewModel.getSaveSearchList()
                } else {
                    viewModel.searchDebounce(
                        changedText = editTextValue ?: ""
                    )
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }
        }

        simpleTextWatcher.let { inputEditText.addTextChangedListener(it) }

        viewModel.getLiveDateState().observe(this) {
            render(it)
        }

        viewModel.observeShowToast().observe(this) { text ->
            showToast(text)
        }

        inputEditText.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus && inputEditText.text.isEmpty()) {
                viewModel.getSaveSearchList()
            }
        }
    }

    private fun selectTrack(track: Track) {
        viewModel.saveTrack(track)
        val trackPlayerIntent = Intent(this@SearchActivity, PlayerActivity::class.java)
        trackPlayerIntent.putExtra(GET_TRACK_PLAYER, track)
        startActivity(trackPlayerIntent)
    }

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }

    private fun render(state: SearchState) {
        when (state) {
            is SearchState.Content -> showContent(state.tracks)
            is SearchState.SaveContent -> showSaveContent(state.tracks)
            is SearchState.Empty -> showEmpty(state.message, state.image)
            is SearchState.Error -> showError(state.errorMessage, state.errorImage)
            is SearchState.Loading -> showLoading()
        }
    }

    private fun showLoading() {
        searchLayout.visibility = View.GONE
        searchLayout.visibility = View.GONE
        recyclerTrack.visibility = View.GONE
        progressBar.visibility = View.VISIBLE
        visibilityError(false)
    }

    private fun showError(errorMessage: String, image: Int) {
        searchLayout.visibility = View.GONE
        recyclerTrack.visibility = View.GONE
        progressBar.visibility = View.GONE
        placeholderMessage.text = errorMessage
        visibilityError(true)
    }

    private fun showEmpty(emptyMessage: String, image: Int) {
        searchLayout.visibility = View.GONE
        recyclerTrack.visibility = View.GONE
        progressBar.visibility = View.GONE
        visibilityError(true)
        updateButton.visibility = View.GONE
        placeholderMessage.text = emptyMessage
        placeholderImage.setImageResource(image)
    }

    private fun showContent(tracks: List<Track>) {
        searchLayout.visibility = View.GONE
        visibilityError(false)
        progressBar.visibility = View.GONE
        recyclerTrack.visibility = View.VISIBLE
        recyclerTrack.adapter = adapter
        adapterData(tracks)
    }

    private fun showSaveContent(tracks: List<Track>) {
        recyclerTrack.visibility = View.GONE
        visibilityError(false)
        progressBar.visibility = View.GONE
        if (tracks.isEmpty()) searchLayout.visibility = View.GONE else searchLayout.visibility =
            View.VISIBLE
        recyclerSearchTrack.adapter = adapter
        adapterData(tracks)
    }

    private fun buttonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    private fun showToast(additionalMessage: String) {
        Toast.makeText(this, additionalMessage, Toast.LENGTH_LONG).show()
    }

    fun adapterData(trackListAdapter: List<Track>) {
        adapter.tracksAdapter.clear()
        adapter.tracksAdapter.addAll(trackListAdapter)
        adapter.notifyDataSetChanged()
    }

    fun visibilityError(s: Boolean) {
        if (s) {
            placeholderMessage.isVisible = true
            placeholderImage.isVisible = true
            updateButton.isVisible = true
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(inputEditText.windowToken, 0)
        } else {
            placeholderMessage.isVisible = false
            placeholderImage.isVisible = false
            updateButton.isVisible = false
        }
    }
}


//    companion object {
//        private const val KEY_TEXT = "KEY_TEXT"
//        private const val TAG = "SPRINT_9"
//        private const val GET_TRACK_PLAYER = "GET_TRACK_PLAYER"
//        private const val CLICK_DEBOUNCE_DELAY = 1000L
//        private const val SEARCH_DEBOUNCE_DELAY = 2000L
//    }
//
//
//
//
//    private val searchRunnable = Runnable { search() }
//    private var isClickAllowed = true
//    private val handler = Handler(Looper.getMainLooper())
//    private val tracksList = ArrayList<Track>()
//    private val adapter = TrackAdapter {
//        if (clickDebounce()) {
//            selectTrack(it)
//        }
//    }
//
//    private lateinit var binding: ActivitySearchBinding
//    private var editTextValue: String? = ""
//    private lateinit var backButton: Toolbar
//    private lateinit var inputEditText: EditText
//    private lateinit var clearButton: ImageView
//    private lateinit var recyclerTrack: RecyclerView
//    private lateinit var recyclerSearchTrack: RecyclerView
//    private lateinit var placeholderMessage: TextView
//    private lateinit var placeholderImage: ImageView
//    private lateinit var updateButton: Button
//    private lateinit var clearSearchListButton: Button
//    private lateinit var progressBar: ProgressBar
//    private lateinit var searchLayout: LinearLayout
//
//    private var trackInteractor = Creator.provideTracksInteractor()
//    private var searchHistory = Creator.provideSearchHistoryInteractor()
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//        binding = ActivitySearchBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//
//        backButton = binding.backMain
//        inputEditText = binding.inputEditText
//        clearButton = binding.clearIcon
//        recyclerTrack = binding.trackList
//        recyclerSearchTrack = binding.searchList
//        placeholderMessage = binding.placeholderMessage
//        placeholderImage = binding.placeholderImage
//        updateButton = binding.updateButton
//        clearSearchListButton = binding.clearSearchList
//        searchLayout = binding.searchLayout
//        progressBar = binding.progressBar
//
//        recyclerTrack.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
//        recyclerSearchTrack.layoutManager =
//            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
//
//        backButton.setOnClickListener {
//            finish()
//        }
//
//        clearButton.setOnClickListener {
//            inputEditText.setText("")
//            inputEditText.clearFocus()
//            visibilityError(false)
//            recyclerTrack.isVisible = false
//            searchLayout.isVisible = false
//            val inputMethodManager =
//                getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
//            inputMethodManager?.hideSoftInputFromWindow(inputEditText.windowToken, 0)
//        }
//
//        clearSearchListButton.setOnClickListener {
//            searchHistory.searchHistoryClear()
//            searchLayout.isVisible = false
//        }
//
//        val simpleTextWatcher = object : TextWatcher {
//            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
//            }
//
//            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//                clearButton.visibility = buttonVisibility(s)
//                editTextValue = s.toString()
//                if (inputEditText.hasFocus() && s?.isEmpty() == true) {
//                    recyclerTrack.isVisible = false
//                    adapterData(searchHistory.searchListFromGson())
//                    searchLayout.visibility = visibilitySearchLayout(true)
//                    visibilityError(false)
//                } else {
//                    searchDebounce()
//                    searchLayout.visibility = visibilitySearchLayout(false)
//                }
//                Log.d(TAG, "textWatcher $editTextValue")
//            }
//
//            override fun afterTextChanged(s: Editable?) {
//            }
//        }
//
//        inputEditText.addTextChangedListener(simpleTextWatcher)
//        inputEditText.setOnEditorActionListener { _, actionId, _ ->
//            if (actionId == EditorInfo.IME_ACTION_DONE) {
//                searchRunnable
//                true
//            }
//            false
//        }
//
//        inputEditText.setOnFocusChangeListener { view, hasFocus ->
//            if (hasFocus && inputEditText.text.isEmpty()) {
//                visibilityError(false)
//                recyclerTrack.isVisible = false
//                adapterData(searchHistory.searchListFromGson())
//                searchLayout.visibility = visibilitySearchLayout(true)
//            } else {
//                searchLayout.visibility = visibilitySearchLayout(false)
//            }
//        }
//
//        updateButton.setOnClickListener {
//            search()
//        }
//    }
//
//    private fun buttonVisibility(s: CharSequence?): Int {
//        return if (s.isNullOrEmpty()) {
//            View.GONE
//        } else {
//            View.VISIBLE
//        }
//    }
//
//    override fun onSaveInstanceState(outState: Bundle) {
//        super.onSaveInstanceState(outState)
//        outState.putString(KEY_TEXT, editTextValue)
//    }
//
//    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
//        super.onRestoreInstanceState(savedInstanceState)
//        editTextValue = savedInstanceState.getString(KEY_TEXT)
//        inputEditText.setText(editTextValue)
//        Log.d(TAG, "onRestoreInstanceState editTextValue")
//    }
//
//    private fun search() {
//        if (inputEditText.text.isNotEmpty()) {
//            visibilityError(false)
//            recyclerTrack.isVisible = false
//            progressBar.isVisible = true
//            val searchText = inputEditText.text.toString()
//            trackInteractor.searchTracks(
//                searchText,
//                object : TracksInteractor.TracksConsumer {
//                    override fun consume(foundTracks: List<Track>?, errorMessage: String?) {
//                        handler.post {
//                            progressBar.isVisible = false
//                            if (foundTracks != null) {
//                                tracksList.clear()
//                                tracksList.addAll(foundTracks)
//                                recyclerTrack.adapter = adapter
//                                adapter.tracksAdapter = tracksList
//                                adapter.notifyDataSetChanged()
//                                recyclerTrack.isVisible = true
//                            }
//                            if (errorMessage != null) {
//                                updateButton.isVisible = true
//                                showMessage(
//                                    getString(R.string.something_went_wrong),
//                                    errorMessage,
//                                    R.drawable.error_image
//                                )
//                            } else if (tracksList.isEmpty()) {
//                                showMessage(
//                                    getString(R.string.nothing_found),
//                                    "",
//                                    R.drawable.no_mode
//                                )
//                            }
//                        }
//                    }
//                })
//        }
//    }
//
//    private fun showMessage(text: String, additionalMessage: String, image: Int) {
//        if (text.isNotEmpty()) {
//            searchLayout.visibility = visibilitySearchLayout(false)
//            recyclerTrack.isVisible = false
//            placeholderMessage.isVisible = true
//            placeholderImage.isVisible = true
//            placeholderMessage.text = text
//            placeholderImage.setImageResource(image)
//            if (additionalMessage.isNotEmpty()) {
//                Toast.makeText(applicationContext, additionalMessage, Toast.LENGTH_LONG)
//                    .show()
//            }
//        } else {
//            placeholderMessage.isVisible = false
//        }
//    }
//
//    private fun selectTrack(track: Track) {
//        searchHistory.addTrackHistory(track)
//        searchHistory.saveSearchList()
//        val trackPlayerIntent = Intent(this@SearchActivity, PlayerActivity::class.java) // поменял класс
//        trackPlayerIntent.putExtra(GET_TRACK_PLAYER, track)
//        inputEditText.clearFocus()
//        searchLayout.isVisible = false
//        startActivity(trackPlayerIntent)
//    }
//
//    fun adapterData(trackListAdapter: List<Track>) {
//        recyclerSearchTrack.adapter = adapter
//        adapter.tracksAdapter = trackListAdapter
//        adapter.notifyDataSetChanged()
//    }
//
//    fun visibilitySearchLayout(s: Boolean): Int {
//        return if (s && searchHistory.searchHistoryTrack().isNotEmpty()) View.VISIBLE
//        else View.GONE
//    }
//
//    fun visibilityError(s: Boolean) {
//        if (s) {
//            placeholderMessage.isVisible = true
//            placeholderImage.isVisible = true
//            updateButton.isVisible = true
//        } else {
//            placeholderMessage.isVisible = false
//            placeholderImage.isVisible = false
//            updateButton.isVisible = false
//        }
//    }
//
//    private fun clickDebounce(): Boolean {
//        val current = isClickAllowed
//        if (isClickAllowed) {
//            isClickAllowed = false
//            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
//        }
//        return current
//    }
//
//    private fun searchDebounce() {
//        handler.removeCallbacks(searchRunnable)
//        handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
//    }
//}