package com.matheusfroes.gamer_guide.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.matheusfroes.gamer_guide.R
import com.matheusfroes.gamer_guide.models.Noticia
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.view_noticia.view.*

/**
 * Created by matheus_froes on 20/09/2017.
 */

class NoticiasAdapter(private val context: Context) : RecyclerView.Adapter<NoticiasAdapter.ViewHolder>() {
    var listener: OnNewsClickListener? = null
    var noticias: List<Noticia> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = View.inflate(context, R.layout.view_noticia, null)
        return ViewHolder(view)
    }

    fun preencherNoticias(noticias: List<Noticia>) {
        this.noticias = noticias
        this.notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val noticia = noticias[position]

        holder.itemView.tvTitulo.text = noticia.titulo

        holder.itemView.setOnClickListener {
            listener?.onClick(noticia)
        }

        Picasso.with(context).load(noticia.imagem).fit().into(holder.itemView.ivImage)
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
