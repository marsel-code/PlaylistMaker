package com.example.playlistmaker

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.PersistableBundle
import android.text.Editable
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
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
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
    private val adapter = TrackAdapter()
    private var editTextValue: String? = ""
    private lateinit var backButton: Toolbar
    private lateinit var inputEditText: EditText
    private lateinit var clearButton: ImageView
    private lateinit var recyclerTrack: RecyclerView
    private lateinit var placeholderMessage: TextView
    private lateinit var placeholderImage: ImageView
    private lateinit var updateButton: Button
    private lateinit var bodyResults: MutableList<Track>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        backButton = findViewById(R.id.backMain)
        inputEditText = findViewById(R.id.inputEditText)
        clearButton = findViewById(R.id.clearIcon)
        recyclerTrack = findViewById(R.id.trackList)
        placeholderMessage = findViewById(R.id.placeholderMessage)
        placeholderImage = findViewById(R.id.placeholderImage)
        updateButton = findViewById(R.id.updateButton)

        adapter.tracksAdapter = tracksList
        recyclerTrack.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerTrack.adapter = adapter

        backButton.setOnClickListener {
            val backButtonIntent = Intent(this, MainActivity::class.java)
            startActivity(backButtonIntent)
        }

        clearButton.setOnClickListener {
            inputEditText.setText("")
            tracksList.clear()
            adapter.notifyDataSetChanged()
            placeholderMessage.visibility = View.GONE
            placeholderImage.visibility = View.GONE
            updateButton.visibility = View.GONE
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(inputEditText.windowToken, 0)
        }

        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                clearButton.visibility = clearButtonVisibility(s)
                editTextValue = s.toString()
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
        updateButton.setOnClickListener {
            search()
            updateButton.visibility = View.GONE
        }
    }

    private fun clearButtonVisibility(s: CharSequence?): Int {
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
        Log.d(TAG, "onRestoreInstanceState ${editTextValue}")
    }

    companion object {
        private const val KEY_TEXT = "KEY_TEXT"
        private const val TAG = "SPRINT_9"
    }

    private fun search() {
        if (inputEditText.text.isNotEmpty()) {
            placeholderMessage.visibility = View.GONE
            placeholderImage.visibility = View.GONE
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
                            adapter.notifyDataSetChanged()
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
}