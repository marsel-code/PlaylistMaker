package com.example.playlistmaker.player.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.databinding.PlayerPlayListItemBinding
import com.example.playlistmaker.media.domain.model.PlayList


class PlayerPlayListAdapter(
    private val clickListener: PlayListClickListener,

    ) : RecyclerView.Adapter<PlayerPlayListViewHolder>() {

    var listPlayList = ArrayList<PlayList>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayerPlayListViewHolder {
        val layoutInspector = LayoutInflater.from(parent.context)
        return PlayerPlayListViewHolder(
            PlayerPlayListItemBinding.inflate(
                layoutInspector,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: PlayerPlayListViewHolder, position: Int) {
        holder.bind(listPlayList[position])
        holder.itemView.setOnClickListener { clickListener.onPlayListClick(listPlayList.get(position)) }
    }

    override fun getItemCount(): Int {
        return listPlayList.size
    }

    fun interface PlayListClickListener {
        fun onPlayListClick(playList: PlayList)
    }
}