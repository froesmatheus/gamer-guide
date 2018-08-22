package com.matheusfroes.gamerguide.ui.feed

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.matheusfroes.gamerguide.R
import com.matheusfroes.gamerguide.data.model.News
import com.matheusfroes.gamerguide.data.models.Noticia
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.view_noticia.view.*

class FeedAdapter2 : RecyclerView.Adapter<FeedAdapter2.ViewHolder>() {
    private var listener: OnNewsClickListener? = null
    var news = emptyList<News>()
    set(value) {
        field = value
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.view_noticia, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val news = news[position]

        holder.bind(news)
    }

    fun setOnClickListener(listener: OnNewsClickListener) {
        this.listener = listener
    }

    override fun getItemCount(): Int = news.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var capaVisivel = true

        init {
            itemView.setOnClickListener {
                val noticia = news[adapterPosition]
                listener?.onClick(noticia)
            }
        }

        fun bind(noticia: News) {
            itemView.tvTitulo.text = noticia.title
            itemView.tvHorarioNoticia.setReferenceTime(noticia.publishDate)

            itemView.tvWebsite.text = "${noticia.website} • "

            capaVisivel = !noticia.image.isEmpty()

            if (capaVisivel) {
                itemView.ivImage.visibility = View.VISIBLE
                Picasso
                        .with(itemView.context)
                        .load(noticia.image)
                        .fit()
                        .centerCrop()
                        .into(itemView.ivImage, object : Callback {
                            override fun onSuccess() {
                                itemView.ivImage.visibility = View.VISIBLE
                            }

                            override fun onError() {
                                itemView.ivImage.visibility = View.GONE
                            }

                        })
            } else {
                itemView.ivImage.visibility = View.GONE
            }


        }
    }

    interface OnNewsClickListener {
        fun onClick(news: News)
    }
}
