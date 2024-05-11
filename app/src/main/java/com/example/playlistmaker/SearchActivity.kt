package com.example.playlistmaker

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
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
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity : AppCompatActivity() {

    private val iTunesBaseUrl = "https://itunes.apple.com"
    private val retrofit = Retrofit.Builder()
        .baseUrl(iTunesBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val iTunesService = retrofit.create(iTunesApi::class.java)
    private val tracksList = mutableListOf<Track>()
    private val adapter = TrackAdapter {
        selectTrack(it)
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
            recyclerTrack.visibility = View.GONE
            searchLayout.visibility = View.GONE
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(inputEditText.windowToken, 0)
        }

        clearSearchListButton.setOnClickListener {
            searchHistoryClass.searchHistoryClear()
            searchLayout.visibility = View.GONE
        }

        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                clearButton.visibility = buttonVisibility(s)
                editTextValue = s.toString()
                if (inputEditText.hasFocus() && s?.isEmpty() == true) {
                    recyclerTrack.visibility = View.GONE
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
                search()
                true
            }
            false
        }

        inputEditText.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus && inputEditText.text.isEmpty()) {
                visibilityError(false)
                recyclerTrack.visibility = View.GONE
                adapterData(searchHistoryClass.searchListFromGson())
                searchLayout.visibility = visibilitySearchLayout(true)
            } else {
                visibilitySearchLayout(false)
            }
        }

        updateButton.setOnClickListener {
            search()
            updateButton.visibility = View.GONE
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

    companion object {
        private const val KEY_TEXT = "KEY_TEXT"
        private const val TAG = "SPRINT_9"
        const val SEARCH_SHARED_PREFERENCES = "search_shared_preferences"
    }

    private fun search() {
        if (inputEditText.text.isNotEmpty()) {
            visibilityError(false)
            iTunesService.search(inputEditText.text.toString()).enqueue(object :
                Callback<iTunesTrackResponse> {
                override fun onResponse(
                    call: Call<iTunesTrackResponse>,
                    response: Response<iTunesTrackResponse>,
                ) {
                    if (response.isSuccessful) {
                        bodyResults = response.body()?.results!!
                        tracksList.clear()
                        if (bodyResults.isNotEmpty()) {
                            tracksList.addAll(bodyResults)
                            recyclerTrack.adapter = adapter
                            adapterData(tracksList)
                            recyclerTrack.visibility = View.VISIBLE
                        }
                        if (tracksList.isEmpty()) {
                            showMessage(getString(R.string.nothing_found), "", R.drawable.no_mode)
                        } else {
                            showMessage("", "", R.drawable.error_image)
                        }
                    } else {
                        updateButton.visibility = View.VISIBLE
                        showMessage(
                            getString(R.string.something_went_wrong),
                            response.code().toString(),
                            R.drawable.error_image
                        )
                    }
                }

                override fun onFailure(call: Call<iTunesTrackResponse>, t: Throwable) {
                    updateButton.visibility = View.VISIBLE
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
            placeholderMessage.visibility = View.VISIBLE
            placeholderImage.visibility = View.VISIBLE
            tracksList.clear()
            adapter.notifyDataSetChanged()
            placeholderMessage.text = text
            placeholderImage.setImageResource(image)
            if (additionalMessage.isNotEmpty()) {
                Toast.makeText(applicationContext, additionalMessage, Toast.LENGTH_LONG)
                    .show()
            }
        } else {
            placeholderMessage.visibility = View.GONE
        }
    }

    private fun selectTrack(track: Track) {
//        val message = "выбран ${track.trackName}\nid ${track.trackId}"
//        Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()
        searchHistoryClass.addTrackHistory(track)
        searchHistoryClass.saveSearchList()
    }

    fun adapterData(trackListAdapter: MutableList<Track>) {
        recyclerSearchTrack.adapter = adapter
        adapter.tracksAdapter = trackListAdapter
        adapter.notifyDataSetChanged()
    }

    fun visibilitySearchLayout(s: Boolean): Int {
        return if (s) {
            if (searchHistoryClass.searchHistory.isNotEmpty()) {
                View.VISIBLE
            } else {
                View.GONE
            }
        } else {
            View.GONE
        }
    }

    fun visibilityError(s: Boolean) {
        if (s) {
            placeholderMessage.visibility = View.VISIBLE
            placeholderImage.visibility = View.VISIBLE
            updateButton.visibility = View.VISIBLE
        } else {
            placeholderMessage.visibility = View.GONE
            placeholderImage.visibility = View.GONE
            updateButton.visibility = View.GONE
        }
    }
}





