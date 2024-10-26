package com.example.playlistmaker.media.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.databinding.TrackItemBinding
import com.example.playlistmaker.search.presentation.model.SearchTrack
import com.example.playlistmaker.search.ui.TrackViewHolder

class PlayListInfoAdapter(private val clickListener: TrackClickListener, private val longClickListener: TrackLongClickListener):  RecyclerView.Adapter<TrackViewHolder>()  {

    var tracksAdapter = ArrayList<SearchTrack>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val layoutInspector = LayoutInflater.from(parent.context)
        return TrackViewHolder(TrackItemBinding.inflate(layoutInspector, parent, false))
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(tracksAdapter.get(position))
        holder.itemView.setOnClickListener { clickListener.onTrackClick(tracksAdapter.get(position)) }
        holder.itemView.setOnLongClickListener { longClickListener.onLongTrackClick(tracksAdapter.get(position)) }
    }

    override fun getItemCount(): Int = tracksAdapter.size

    fun interface TrackClickListener {
        fun onTrackClick(track: SearchTrack)
    }

    fun interface TrackLongClickListener {
        fun onLongTrackClick(track: SearchTrack): Boolean
    }



}