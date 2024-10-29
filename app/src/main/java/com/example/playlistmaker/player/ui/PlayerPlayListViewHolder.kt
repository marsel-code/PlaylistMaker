package com.example.playlistmaker.player.ui

import android.util.TypedValue
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.PlayerPlayListItemBinding
import com.example.playlistmaker.media.domain.model.PlayList


class PlayerPlayListViewHolder(binding: PlayerPlayListItemBinding) :
    RecyclerView.ViewHolder(binding.root) {
    private var playListNme: TextView = binding.playListName
    private var numberTacks: TextView = binding.numberTracks
    private var playListImage: ImageView = binding.playListImage


    fun bind(playList: PlayList) {
        playListNme.text = playList.playListName
        numberTacks.text = itemView.context.resources.getQuantityString(
        R.plurals.plurals_track,
        playList.numberTracks.toInt(), playList.numberTracks.toInt()
        )

        Glide.with(itemView)
            .load(playList.artworkUri)
            .placeholder(R.drawable.no_reply)
            .transform(
                MultiTransformation(
                    CenterCrop(),
                    RoundedCorners(
                        TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_DIP,
                            2F,
                            playListImage.resources.displayMetrics
                        ).toInt()
                    )
                )
            )
            .into(playListImage)
    }
}