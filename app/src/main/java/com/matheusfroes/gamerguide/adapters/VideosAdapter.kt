package com.matheusfroes.gamerguide.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.matheusfroes.gamerguide.R
import com.matheusfroes.gamerguide.models.Video
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.view_video.view.*

/**
 * Created by matheusfroes on 30/09/2017.
 */
class VideosAdapter(val context: Context) : RecyclerView.Adapter<VideosAdapter.ViewHolder>() {
    private var videos: List<Video> = mutableListOf()
    private var listener: OnVideoClickListener? = null

    fun preencherLista(listas: List<Video>) {
        this.videos = listas
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
            ViewHolder(LayoutInflater.from(context).inflate(R.layout.view_video, parent, false))

    override fun getItemCount() = videos.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val video = videos[position]

        holder.itemView.tvNomeVideo.text = video.nome

        holder.itemView.setOnClickListener { listener?.onVideoClick(video) }

        val thumbnailUrl = context.getString(R.string.youtube_thumbnail_url, video.videoId)
        Picasso
                .with(context)
                .load(thumbnailUrl)
                .fit()
                .into(holder.itemView.ivThumbnailVideo)
    }

    fun setOnVideoClickListener(listener: OnVideoClickListener) {
        this.listener = listener
    }

    inner class ViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView)

    interface OnVideoClickListener {
        fun onVideoClick(video: Video)
    }
}