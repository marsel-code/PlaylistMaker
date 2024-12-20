package com.example.playlistmaker.media.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.databinding.PlayListItemBinding
import com.example.playlistmaker.media.domain.model.PlayList

class PlayListAdapter(private val clickListener: PlayListClickListener) :
    RecyclerView.Adapter<PlayListViewHolder>() {

     val listPlayList = ArrayList<PlayList>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayListViewHolder {
        val layoutInspector = LayoutInflater.from(parent.context)
        return PlayListViewHolder(PlayListItemBinding.inflate(layoutInspector, parent, false))
    }

    override fun getItemCount(): Int {
        return listPlayList.size
    }

    override fun onBindViewHolder(holder: PlayListViewHolder, position: Int) {
        holder.bind(listPlayList[position])
        holder.itemView.setOnClickListener { clickListener.onPlayListClick(listPlayList.get(position)) }
    }

    fun interface PlayListClickListener {
        fun onPlayListClick(playList: PlayList)
    }

}