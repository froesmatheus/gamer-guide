package com.matheusfroes.gamer_guide.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.matheusfroes.gamer_guide.R
import com.matheusfroes.gamer_guide.models.Lista
import kotlinx.android.synthetic.main.view_lista.view.*

/**
 * Created by matheus_froes on 21/09/2017.
 */
class ListasAdapter(private val context: Context, val listas: List<Lista>) : RecyclerView.Adapter<ListasAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        return ViewHolder(View.inflate(context, R.layout.view_lista, null))
    }

    override fun getItemCount() = listas.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val lista = listas[position]

        holder.itemView.tvNome.text = lista.nome
        holder.itemView.tvQuantidadeJogos.text = "0 jogos"
    }


    inner class ViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView)
}