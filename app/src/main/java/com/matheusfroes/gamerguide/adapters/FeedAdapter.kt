package com.matheusfroes.gamerguide.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.matheusfroes.gamerguide.R
import com.matheusfroes.gamerguide.models.Noticia
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.view_noticia.view.*

/**
 * Created by matheus_froes on 20/09/2017.
 */

class FeedAdapter(private val context: Context) : RecyclerView.Adapter<FeedAdapter.ViewHolder>() {
    private var listener: OnNewsClickListener? = null
    private var noticias: MutableList<Noticia> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.view_noticia, parent, false)
        return ViewHolder(view)
    }

    fun preencherNoticias(noticias: MutableList<Noticia>) {
        this.noticias.addAll(noticias)
        this.notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val noticia = noticias[position]

        holder.itemView.tvTitulo.text = noticia.titulo
        holder.itemView.tvHorarioNoticia.setReferenceTime(noticia.dataPublicacao)

        holder.itemView.tvWebsite.text = "${noticia.website} • "

        holder.itemView.setOnClickListener {
            listener?.onClick(noticia)
        }

        Picasso.with(context).load(noticia.imagem).fit().centerCrop().into(holder.itemView.ivImage)
    }

    fun setOnClickListener(listener: OnNewsClickListener) {
        this.listener = listener
    }

    override fun getItemCount(): Int = noticias.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    interface OnNewsClickListener {
        fun onClick(noticia: Noticia)
    }
}
