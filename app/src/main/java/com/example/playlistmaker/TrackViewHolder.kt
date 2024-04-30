package com.example.playlistmaker

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import java.text.SimpleDateFormat
import java.util.Locale

class TrackViewHolder(parent: ViewGroup) :
    RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context)
            .inflate(R.layout.track_item, parent, false)
    ) {

    private var trackNameView: TextView = itemView.findViewById(R.id.trackName)
    private var artistNameView: TextView = itemView.findViewById(R.id.trackArtist)
    private var trackTimeView: TextView = itemView.findViewById(R.id.trackTime)
    private var artworkUrl100View: ImageView = itemView.findViewById(R.id.trackImage)
    private val dateFormat by lazy { SimpleDateFormat("mm:ss", Locale.getDefault()) }

    fun bind(model: Track) {
        trackNameView.text = model.trackName
        artistNameView.text = model.artistName
        trackTimeView.text = dateFormat.format(model.trackTimeMillis)
        Glide.with(itemView)
            .load(model.artworkUrl100)
            .placeholder(R.drawable.no_reply)
            .centerCrop()
            .transform(RoundedCorners(2))
            .into(artworkUrl100View)
    }
}