package com.example.playlistmaker.media.ui

import android.util.TypedValue
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.PlayListItemBinding

import com.example.playlistmaker.media.domain.model.PlayList

class PlayListViewHolder(private val binding: PlayListItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    private val image: ImageView = binding.playListImage
    private val title: TextView = binding.title
    private val numberTracks: TextView = binding.numberTracks

    fun bind(playList: PlayList) {
        title.text = playList.playListName
        numberTracks.text = itemView.context.resources.getQuantityString(
            R.plurals.plurals_track,
            playList.numberTracks.toInt(), playList.numberTracks.toInt()
        )

        Glide.with(itemView)
            .load(playList.artworkUri)
            .placeholder(R.drawable.no_reply)
            .centerCrop()
            .transform(
                MultiTransformation(
                    CenterCrop(),
                    RoundedCorners(
                        TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_DIP,
                            8F,
                            binding.playListImage.resources.displayMetrics
                        ).toInt()
                    )
                )
            )
            .into(image)
    }
}