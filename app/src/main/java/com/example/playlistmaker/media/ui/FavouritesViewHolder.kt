package com.example.playlistmaker.media.ui

import android.util.TypedValue
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.TrackItemBinding
import com.example.playlistmaker.search.presentation.model.SearchTrack

class FavouritesViewHolder(binding: TrackItemBinding) :
    RecyclerView.ViewHolder(binding.root) {
    private var trackNameView: TextView = binding.trackName
    private var artistNameView: TextView = binding.trackArtist
    private var trackTimeView: TextView = binding.trackTime
    private var artworkUrl100View: ImageView = binding.trackImage

    fun bind(model: SearchTrack) {
        trackNameView.text = model.trackName
        artistNameView.text = model.artistName
        trackTimeView.text = model.trackTimeMillis
        Glide.with(itemView)
            .load(model.artworkUrl100)
            .placeholder(R.drawable.no_reply)
            .centerCrop()
            .transform(
                RoundedCorners(
                    TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP,
                        2F,
                        itemView.resources.displayMetrics
                    ).toInt()
                )
            )
            .into(artworkUrl100View)
    }
}


