package com.matheusfroes.gamerguide.ui.gamedetails.video

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.matheusfroes.gamerguide.R
import com.matheusfroes.gamerguide.data.model.Video
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.view_video.view.*

class VideosAdapter : RecyclerView.Adapter<VideosAdapter.ViewHolder>() {
    var videos = listOf<Video>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var videoClick: ((video: Video) -> Any)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
            ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.view_video, parent, false))

    override fun getItemCount() = videos.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val video = videos[position]

        holder.bind(video)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {
            itemView.setOnClickListener {
                val video = videos[adapterPosition]
                videoClick?.invoke(video)
            }
        }

        fun bind(video: Video) {
            itemView.tvNomeVideo.text = video.name

            val thumbnailUrl = itemView.context.getString(R.string.youtube_thumbnail_url, video.videoId)
            Picasso
                    .with(itemView.context)
                    .load(thumbnailUrl)
                    .fit()
                    .into(itemView.ivThumbnailVideo)
        }
    }
}