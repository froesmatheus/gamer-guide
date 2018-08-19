package com.matheusfroes.gamerguide.ui.feed

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.matheusfroes.gamerguide.R
import com.matheusfroes.gamerguide.data.models.Noticia
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.view_noticia.view.*

class FeedAdapter : RecyclerView.Adapter<FeedAdapter.ViewHolder>() {
    private var listener: OnNewsClickListener? = null
    private var noticias: MutableList<Noticia> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.view_noticia, parent, false)
        return ViewHolder(view)
    }

    fun preencherNoticias(noticias: MutableList<Noticia>) {
        this.noticias = noticias
        this.notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val noticia = noticias[position]

        holder.bind(noticia)
    }

    fun setOnClickListener(listener: OnNewsClickListener) {
        this.listener = listener
    }

    override fun getItemCount(): Int = noticias.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var capaVisivel = true

        init {
            itemView.setOnClickListener {
                val noticia = noticias[adapterPosition]
                listener?.onClick(noticia)
            }
        }

        fun bind(noticia: Noticia) {
            itemView.tvTitulo.text = noticia.titulo
            itemView.tvHorarioNoticia.setReferenceTime(noticia.dataPublicacao)

            itemView.tvWebsite.text = "${noticia.website} • "

            capaVisivel = !noticia.imagem.isEmpty()

            if (capaVisivel) {
                itemView.ivImage.visibility = View.VISIBLE
                Picasso
                        .with(itemView.context)
                        .load(noticia.imagem)
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
        fun onClick(noticia: Noticia)
    }
}
