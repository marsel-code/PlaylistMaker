package com.example.playlistmaker

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class TrackAdapter(val clickListener: TrackClickListener) :
    RecyclerView.Adapter<TrackViewHolder>() {

    var tracksAdapter = mutableListOf<Track>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder =
        TrackViewHolder(parent)

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(tracksAdapter.get(position))
        holder.itemView.setOnClickListener { clickListener.onTrackClick(tracksAdapter.get(position)) }
    }

    override fun getItemCount(): Int = tracksAdapter.size

    fun interface TrackClickListener {
        fun onTrackClick(track: Track)
    }
}
