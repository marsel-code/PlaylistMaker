package com.example.playlistmaker

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.Layout
import android.text.TextWatcher
import android.util.Log
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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity : AppCompatActivity() {

    companion object {
        private const val KEY_TEXT = "KEY_TEXT"
        private const val TAG = "SPRINT_9"
        const val SEARCH_SHARED_PREFERENCES = "search_shared_preferences"
        private const val GET_TRACK_PLAYER = "GET_TRACK_PLAYER"
        private const val CLICK_DEBOUNCE_DELAY = 1000L
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }

    private val searchRunnable = Runnable { search() }
    private var isClickAllowed = true
    private val handler = Handler(Looper.getMainLooper())
    private val iTunesBaseUrl = "https://itunes.apple.com"
    private val retrofit = Retrofit.Builder()
        .baseUrl(iTunesBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val iTunesService = retrofit.create(iTunesApi::class.java)
    private val tracksList = mutableListOf<Track>()
    private val adapter = TrackAdapter {
        if (clickDebounce()) {
            selectTrack(it)
        }
    }

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
    private lateinit var bodyResults: MutableList<Track>
    private lateinit var searchHistoryClass: SearchHistory
    private lateinit var sharedPrefsSearch: SharedPreferences
    private lateinit var searchLayout: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        sharedPrefsSearch = getSharedPreferences(SEARCH_SHARED_PREFERENCES, MODE_PRIVATE)
        searchHistoryClass = SearchHistory(sharedPrefsSearch)

        backButton = findViewById(R.id.backMain)
        inputEditText = findViewById(R.id.inputEditText)
        clearButton = findViewById(R.id.clearIcon)
        recyclerTrack = findViewById(R.id.trackList)
        recyclerSearchTrack = findViewById(R.id.searchList)
        placeholderMessage = findViewById(R.id.placeholderMessage)
        placeholderImage = findViewById(R.id.placeholderImage)
        updateButton = findViewById(R.id.updateButton)
        clearSearchListButton = findViewById(R.id.clearSearchList)
        searchLayout = findViewById(R.id.searchLayout)
        progressBar = findViewById(R.id.progressBar)

        recyclerTrack.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerSearchTrack.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        backButton.setOnClickListener {
            finish()
        }

        clearButton.setOnClickListener {
            inputEditText.setText("")
            inputEditText.clearFocus()
            visibilityError(false)
            recyclerTrack.isVisible = false
            searchLayout.isVisible = false
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(inputEditText.windowToken, 0)
        }

        clearSearchListButton.setOnClickListener {
            searchHistoryClass.searchHistoryClear()
            searchLayout.isVisible = false
        }

        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                clearButton.visibility = buttonVisibility(s)
                editTextValue = s.toString()
                searchDebounce()
                if (inputEditText.hasFocus() && s?.isEmpty() == true) {
                    recyclerTrack.isVisible = false
                    searchLayout.visibility = visibilitySearchLayout(true)
                    adapterData(searchHistoryClass.searchListFromGson())
                    visibilityError(false)
                } else {
                    searchLayout.visibility = visibilitySearchLayout(false)
                }
                Log.d(TAG, "textWatcher $editTextValue")
            }

            override fun afterTextChanged(s: Editable?) {
            }
        }

        inputEditText.addTextChangedListener(simpleTextWatcher)
        inputEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                searchRunnable
                true
            }
            false
        }

        inputEditText.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus && inputEditText.text.isEmpty()) {
                visibilityError(false)
                recyclerTrack.isVisible = false
                adapterData(searchHistoryClass.searchListFromGson())
                searchLayout.visibility = visibilitySearchLayout(true)
            } else {
                visibilitySearchLayout(false)
            }
        }

        updateButton.setOnClickListener {
            searchRunnable
            updateButton.isVisible = false
        }
    }

    private fun buttonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(KEY_TEXT, editTextValue)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        editTextValue = savedInstanceState.getString(KEY_TEXT)
        inputEditText.setText(editTextValue)
        Log.d(TAG, "onRestoreInstanceState editTextValue")
    }


    private fun search() {
        if (inputEditText.text.isNotEmpty()) {
            visibilityError(false)
            recyclerTrack.isVisible = false
            progressBar.isVisible = true
            iTunesService.search(inputEditText.text.toString()).enqueue(object :
                Callback<iTunesTrackResponse> {
                override fun onResponse(
                    call: Call<iTunesTrackResponse>,
                    response: Response<iTunesTrackResponse>,
                ) {
                    progressBar.visibility = View.GONE
                    recyclerTrack.isVisible = true
                    if (response.isSuccessful) {
                        bodyResults = response.body()?.results!!
                        tracksList.clear()
                        if (bodyResults.isNotEmpty()) {
                            tracksList.addAll(bodyResults)
                            recyclerTrack.adapter = adapter
                            adapterData(tracksList)
                            recyclerTrack.isVisible = true
                        }
                        if (tracksList.isEmpty()) {
                            showMessage(getString(R.string.nothing_found), "", R.drawable.no_mode)
                        } else {
                            showMessage("", "", R.drawable.error_image)
                        }
                    } else {
                        updateButton.isVisible = true
                        showMessage(
                            getString(R.string.something_went_wrong),
                            response.code().toString(),
                            R.drawable.error_image
                        )
                    }
                }

                override fun onFailure(call: Call<iTunesTrackResponse>, t: Throwable) {
                    updateButton.isVisible = true
                    progressBar.visibility = View.GONE
                    showMessage(
                        getString(R.string.something_went_wrong),
                        t.message.toString(),
                        R.drawable.error_image
                    )
                }
            })
        }
    }

    private fun showMessage(text: String, additionalMessage: String, image: Int) {
        if (text.isNotEmpty()) {
            placeholderMessage.isVisible = true
            placeholderImage.isVisible = true
            tracksList.clear()
            adapter.notifyDataSetChanged()
            placeholderMessage.text = text
            placeholderImage.setImageResource(image)
            if (additionalMessage.isNotEmpty()) {
                Toast.makeText(applicationContext, additionalMessage, Toast.LENGTH_LONG)
                    .show()
            }
        } else {
            placeholderMessage.isVisible = false
        }
    }

    private fun selectTrack(track: Track) {
        searchHistoryClass.addTrackHistory(track)
        searchHistoryClass.saveSearchList()
        val trackPlayerIntent = Intent(this@SearchActivity, AudioPlayerActivity::class.java)
        trackPlayerIntent.putExtra(GET_TRACK_PLAYER, track)
        inputEditText.clearFocus()
        searchLayout.isVisible = false
        startActivity(trackPlayerIntent)
    }

    fun adapterData(trackListAdapter: MutableList<Track>) {
        recyclerSearchTrack.adapter = adapter
        adapter.tracksAdapter = trackListAdapter
        adapter.notifyDataSetChanged()
    }

    fun visibilitySearchLayout(s: Boolean): Int {
        return if (s && searchHistoryClass.searchHistory.isNotEmpty()) View.VISIBLE
        else View.GONE
    }

    fun visibilityError(s: Boolean) {
        if (s) {
            placeholderMessage.isVisible = true
            placeholderImage.isVisible = true
            updateButton.isVisible = true
        } else {
            placeholderMessage.isVisible = false
            placeholderImage.isVisible = false
            updateButton.isVisible = false
        }
    }

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }

    private fun searchDebounce() {
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
    }
}





