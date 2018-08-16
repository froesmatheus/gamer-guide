package com.matheusfroes.gamerguide.ui.gamedetails.livestream

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.matheusfroes.gamerguide.R
import com.matheusfroes.gamerguide.network.data.Stream
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.view_stream.view.*

class StreamsAdapter : RecyclerView.Adapter<StreamsAdapter.ViewHolder>() {
    var streams = mutableListOf<Stream>()
        set(value) {
            field.addAll(value)
            notifyDataSetChanged()
        }
    var streamClick: ((stream: Stream) -> Any)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
            ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.view_stream, parent, false))

    override fun getItemCount() = streams.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val stream = streams[position]

        holder.bind(stream)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(stream: Stream) {
            itemView.tvViewers.text = itemView.context.getString(R.string.espectadores_stream, stream.viewers)
            itemView.tvTituloStream.text = stream.channel.status
            itemView.tvNomeStreamer.text = stream.channel.displayName

            Picasso.with(itemView.context)
                    .load(stream.preview.large)
                    .fit()
                    .into(itemView.ivPreviewStream)

            Picasso.with(itemView.context).load(stream.channel.logo).fit().into(itemView.ivStreamerLogo)
        }

        init {
            itemView.setOnClickListener {
                val stream = streams[adapterPosition]
                streamClick?.invoke(stream)
            }
        }
    }
}