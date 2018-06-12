package com.matheusfroes.gamerguide.ui.feed

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.matheusfroes.gamerguide.R
import com.matheusfroes.gamerguide.data.model.News
import com.squareup.picasso.Picasso

class FeedAdapter(private val context: Context) : RecyclerView.Adapter<FeedAdapter.ViewHolder>() {
    private var listener: OnNewsClickListener? = null
    private var noticias: MutableList<News> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.view_noticia, parent, false)
        return ViewHolder(view)
    }

    fun preencherNoticias(noticias: MutableList<News>) {
        this.noticias = noticias
        this.notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val noticia = noticias[position]

        holder.itemView.tvTitulo.text = noticia.title
        holder.itemView.tvHorarioNoticia.setReferenceTime(noticia.publishDate)

        holder.itemView.tvWebsite.text = "${noticia.website} • "

        holder.capaVisivel = !noticia.image.isEmpty()

        if (holder.capaVisivel) {
            holder.itemView.ivImage.visibility = View.VISIBLE
            Picasso.with(context).load(noticia.image).fit().centerCrop().into(holder.itemView.ivImage)
        } else {
            holder.itemView.ivImage.visibility = View.GONE
        }
    }

    fun setOnClickListener(listener: OnNewsClickListener) {
        this.listener = listener
    }

    override fun getItemCount(): Int = noticias.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var capaVisivel = true

        init {
            itemView.setOnClickListener {
                val noticia = noticias[adapterPosition]
                listener?.onClick(noticia)
            }
        }
    }

    interface OnNewsClickListener {
        fun onClick(noticia: News)
    }
}
