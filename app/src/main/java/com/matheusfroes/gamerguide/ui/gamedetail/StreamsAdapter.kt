package com.matheusfroes.gamerguide.ui.gamedetail

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.matheusfroes.gamerguide.R
import com.matheusfroes.gamerguide.network.data.Stream
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.view_stream.view.*

class StreamsAdapter(val context: Context) : RecyclerView.Adapter<StreamsAdapter.ViewHolder>() {
    private var streams: MutableList<Stream> = mutableListOf()
    private var listener: OnStreamClickListener? = null

    fun preencherLista(listas: List<Stream>) {
        this.streams.addAll(listas)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
            ViewHolder(LayoutInflater.from(context).inflate(R.layout.view_stream, parent, false))

    override fun getItemCount() = streams.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val stream = streams[position]

        holder.itemView.tvViewers.text = context.getString(R.string.espectadores_stream, stream.viewers)
        holder.itemView.tvTituloStream.text = stream.channel.status
        holder.itemView.tvNomeStreamer.text = stream.channel.displayName

        //holder.itemView.setOnClickListener { listener?.onStreamClick(stream) }

        Picasso.with(context)
                .load(stream.preview.large)
                .fit()
                .into(holder.itemView.ivPreviewStream)

        Picasso.with(context).load(stream.channel.logo).fit().into(holder.itemView.ivStreamerLogo)
    }

    fun setOnStreamClickListener(listener: OnStreamClickListener) {
        this.listener = listener
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        init {
            itemView.setOnClickListener {
                val stream = streams[adapterPosition]
                listener?.onStreamClick(stream)
            }
        }

    }

    interface OnStreamClickListener {
        fun onStreamClick(stream: Stream)
    }
}