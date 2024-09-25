package com.example.playlistmaker.media.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.databinding.TrackItemBinding
import com.example.playlistmaker.search.presentation.model.SearchTrack

class FavouritesAdapter(private val clickListener: TrackClickListener) :
    RecyclerView.Adapter<FavouritesViewHolder>() {

    var tracksAdapter = ArrayList<SearchTrack>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouritesViewHolder {
        val layoutInspector = LayoutInflater.from(parent.context)
        return FavouritesViewHolder(TrackItemBinding.inflate(layoutInspector, parent, false))
    }

    override fun onBindViewHolder(holder: FavouritesViewHolder, position: Int) {
        holder.bind(tracksAdapter.get(position))
        holder.itemView.setOnClickListener { clickListener.onTrackClick(tracksAdapter.get(position)) }
    }

    override fun getItemCount(): Int = tracksAdapter.size

    fun interface TrackClickListener {
        fun onTrackClick(track: SearchTrack)
    }
   }
