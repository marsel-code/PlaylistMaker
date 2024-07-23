package com.example.playlistmaker.search.presentation.view_model


import android.app.Application
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.R
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.util.SingleLiveEvent
import com.example.playlistmaker.search.domain.TracksInteractor
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.search.presentation.state.SearchState


class SearchViewModel(application: Application) : AndroidViewModel(application) {

    private val handler = Handler(Looper.getMainLooper())

    private var trackInteractor = Creator.provideTracksInteractor()
    private var searchHistory = Creator.provideSearchHistoryInteractor()

    private val stateLiveData = MutableLiveData<SearchState>()
    fun getLiveDateState(): LiveData<SearchState> = stateLiveData

    private val showToast = SingleLiveEvent<String>()
    fun observeShowToast(): LiveData<String> = showToast

    var latestSearchText: String? = null

    fun searchDebounce(changedText: String) {
        if (latestSearchText == changedText) {
            return
        }
        this.latestSearchText = changedText
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)
        val searchRunnable = Runnable { search(changedText) }
        val postTime = SystemClock.uptimeMillis() + SEARCH_DEBOUNCE_DELAY
        handler.postAtTime(
            searchRunnable,
            SEARCH_REQUEST_TOKEN,
            postTime,
        )
    }

    fun search(newSearchText: String) {
        if (newSearchText.isNotEmpty()) {
            renderState((SearchState.Loading))
            trackInteractor.searchTracks(
                newSearchText,
                object : TracksInteractor.TracksConsumer {
                    override fun consume(foundTracks: List<Track>?, errorMessage: String?) {
                        val tracks = mutableListOf<Track>()
                        if (foundTracks != null) {
                            tracks.addAll(foundTracks)
                        }
                        when {
                            errorMessage != null -> {
                                renderState(
                                    SearchState.Error(
                                        errorMessage = getApplication<Application>().getString(
                                            R.string.something_went_wrong
                                        ),
                                        errorImage = R.drawable.error_image
                                    )
                                )
                                latestSearchText = null
                                showToast.postValue("ошибка связи")
                            }

                            tracks.isEmpty() -> {
                                renderState(
                                    SearchState.Empty(
                                        message = getApplication<Application>().getString(R.string.nothing_found),
                                        image = R.drawable.no_mode
                                    )
                                )
                            }

                            else -> {
                                renderState(SearchState.Content(tracks))
                            }
                        }
                    }
                }
            )
        }
    }

    override fun onCleared() {
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)
    }

    private fun renderState(state: SearchState) {
        stateLiveData.postValue(state)
    }

    fun saveTrack(track: Track) {
        searchHistory.addTrackHistory(track)
        searchHistory.saveSearchList()
    }

    fun searchHistoryClear() {
        searchHistory.searchHistoryClear()
    }

    fun getSaveSearchList() {
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)
        renderState(SearchState.SaveContent(searchHistory.searchListFromGson()))
    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private val SEARCH_REQUEST_TOKEN = Any()
        fun getViewModelFactory(): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                SearchViewModel(this[APPLICATION_KEY] as Application)
            }
        }
    }
}


//
//companion object {
//    private const val KEY_TEXT = "KEY_TEXT"
//    private const val TAG = "SPRINT_9"
//    private const val GET_TRACK_PLAYER = "GET_TRACK_PLAYER"
//    private const val CLICK_DEBOUNCE_DELAY = 1000L
//    private const val SEARCH_DEBOUNCE_DELAY = 2000L
//}
//
//
//
//
//private val searchRunnable = Runnable { search() }
//private var isClickAllowed = true
//private val handler = Handler(Looper.getMainLooper())
//private val tracksList = ArrayList<Track>()
//private val adapter = TrackAdapter {
//    if (clickDebounce()) {
//        selectTrack(it)
//    }
//}
//
//private lateinit var binding: ActivitySearchBinding
//private var editTextValue: String? = ""
//private lateinit var backButton: Toolbar
//private lateinit var inputEditText: EditText
//private lateinit var clearButton: ImageView
//private lateinit var recyclerTrack: RecyclerView
//private lateinit var recyclerSearchTrack: RecyclerView
//private lateinit var placeholderMessage: TextView
//private lateinit var placeholderImage: ImageView
//private lateinit var updateButton: Button
//private lateinit var clearSearchListButton: Button
//private lateinit var progressBar: ProgressBar
//private lateinit var searchLayout: LinearLayout
//
//private var trackInteractor = Creator.provideTracksInteractor()
//private var searchHistory = Creator.provideSearchHistoryInteractor()
//
//override fun onCreate(savedInstanceState: Bundle?) {
//    super.onCreate(savedInstanceState)
//
//    binding = ActivitySearchBinding.inflate(layoutInflater)
//    setContentView(binding.root)
//
//    backButton = binding.backMain
//    inputEditText = binding.inputEditText
//    clearButton = binding.clearIcon
//    recyclerTrack = binding.trackList
//    recyclerSearchTrack = binding.searchList
//    placeholderMessage = binding.placeholderMessage
//    placeholderImage = binding.placeholderImage
//    updateButton = binding.updateButton
//    clearSearchListButton = binding.clearSearchList
//    searchLayout = binding.searchLayout
//    progressBar = binding.progressBar
//
//    recyclerTrack.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
//    recyclerSearchTrack.layoutManager =
//        LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
//
//    backButton.setOnClickListener {
//        finish()
//    }
//
//    clearButton.setOnClickListener {
//        inputEditText.setText("")
//        inputEditText.clearFocus()
//        visibilityError(false)
//        recyclerTrack.isVisible = false
//        searchLayout.isVisible = false
//        val inputMethodManager =
//            getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
//        inputMethodManager?.hideSoftInputFromWindow(inputEditText.windowToken, 0)
//    }
//
//    clearSearchListButton.setOnClickListener {
//        searchHistory.searchHistoryClear()
//        searchLayout.isVisible = false
//    }
//
//    val simpleTextWatcher = object : TextWatcher {
//        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
//        }
//
//        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//            clearButton.visibility = buttonVisibility(s)
//            editTextValue = s.toString()
//            if (inputEditText.hasFocus() && s?.isEmpty() == true) {
//                recyclerTrack.isVisible = false
//                adapterData(searchHistory.searchListFromGson())
//                searchLayout.visibility = visibilitySearchLayout(true)
//                visibilityError(false)
//            } else {
//                searchDebounce()
//                searchLayout.visibility = visibilitySearchLayout(false)
//            }
//            Log.d(TAG, "textWatcher $editTextValue")
//        }
//
//        override fun afterTextChanged(s: Editable?) {
//        }
//    }
//
//    inputEditText.addTextChangedListener(simpleTextWatcher)
//    inputEditText.setOnEditorActionListener { _, actionId, _ ->
//        if (actionId == EditorInfo.IME_ACTION_DONE) {
//            searchRunnable
//            true
//        }
//        false
//    }
//
//    inputEditText.setOnFocusChangeListener { view, hasFocus ->
//        if (hasFocus && inputEditText.text.isEmpty()) {
//            visibilityError(false)
//            recyclerTrack.isVisible = false
//            adapterData(searchHistory.searchListFromGson())
//            searchLayout.visibility = visibilitySearchLayout(true)
//        } else {
//            searchLayout.visibility = visibilitySearchLayout(false)
//        }
//    }
//
//    updateButton.setOnClickListener {
//        search()
//    }
//}
//
//private fun buttonVisibility(s: CharSequence?): Int {
//    return if (s.isNullOrEmpty()) {
//        View.GONE
//    } else {
//        View.VISIBLE
//    }
//}
//
//override fun onSaveInstanceState(outState: Bundle) {
//    super.onSaveInstanceState(outState)
//    outState.putString(KEY_TEXT, editTextValue)
//}
//
//override fun onRestoreInstanceState(savedInstanceState: Bundle) {
//    super.onRestoreInstanceState(savedInstanceState)
//    editTextValue = savedInstanceState.getString(KEY_TEXT)
//    inputEditText.setText(editTextValue)
//    Log.d(TAG, "onRestoreInstanceState editTextValue")
//}
//
//private fun search() {
//    if (inputEditText.text.isNotEmpty()) {
//        visibilityError(false)
//        recyclerTrack.isVisible = false
//        progressBar.isVisible = true
//        val searchText = inputEditText.text.toString()
//        trackInteractor.searchTracks(
//            searchText,
//            object : TracksInteractor.TracksConsumer {
//                override fun consume(foundTracks: List<Track>?, errorMessage: String?) {
//                    handler.post {
//                        progressBar.isVisible = false
//                        if (foundTracks != null) {
//                            tracksList.clear()
//                            tracksList.addAll(foundTracks)
//                            recyclerTrack.adapter = adapter
//                            adapter.tracksAdapter = tracksList
//                            adapter.notifyDataSetChanged()
//                            recyclerTrack.isVisible = true
//                        }
//                        if (errorMessage != null) {
//                            updateButton.isVisible = true
//                            showMessage(
//                                getString(R.string.something_went_wrong),
//                                errorMessage,
//                                R.drawable.error_image
//                            )
//                        } else if (tracksList.isEmpty()) {
//                            showMessage(
//                                getString(R.string.nothing_found),
//                                "",
//                                R.drawable.no_mode
//                            )
//                        }
//                    }
//                }
//            })
//    }
//}
//
//private fun showMessage(text: String, additionalMessage: String, image: Int) {
//    if (text.isNotEmpty()) {
//        searchLayout.visibility = visibilitySearchLayout(false)
//        recyclerTrack.isVisible = false
//        placeholderMessage.isVisible = true
//        placeholderImage.isVisible = true
//        placeholderMessage.text = text
//        placeholderImage.setImageResource(image)
//        if (additionalMessage.isNotEmpty()) {
//            Toast.makeText(applicationContext, additionalMessage, Toast.LENGTH_LONG)
//                .show()
//        }
//    } else {
//        placeholderMessage.isVisible = false
//    }
//}
//
//private fun selectTrack(track: Track) {
//    searchHistory.addTrackHistory(track)
//    searchHistory.saveSearchList()
//    val trackPlayerIntent = Intent(this@SearchActivity, PlayerActivity::class.java) // поменял класс
//    trackPlayerIntent.putExtra(GET_TRACK_PLAYER, track)
//    inputEditText.clearFocus()
//    searchLayout.isVisible = false
//    startActivity(trackPlayerIntent)
//}
//
//fun adapterData(trackListAdapter: List<Track>) {
//    recyclerSearchTrack.adapter = adapter
//    adapter.tracksAdapter = trackListAdapter
//    adapter.notifyDataSetChanged()
//}
//
//fun visibilitySearchLayout(s: Boolean): Int {
//    return if (s && searchHistory.searchHistoryTrack().isNotEmpty()) View.VISIBLE
//    else View.GONE
//}
//
//fun visibilityError(s: Boolean) {
//    if (s) {
//        placeholderMessage.isVisible = true
//        placeholderImage.isVisible = true
//        updateButton.isVisible = true
//    } else {
//        placeholderMessage.isVisible = false
//        placeholderImage.isVisible = false
//        updateButton.isVisible = false
//    }
//}
//
//private fun clickDebounce(): Boolean {
//    val current = isClickAllowed
//    if (isClickAllowed) {
//        isClickAllowed = false
//        handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
//    }
//    return current
//}
//
//private fun searchDebounce() {
//    handler.removeCallbacks(searchRunnable)
//    handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
//}
//}