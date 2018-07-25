package com.matheusfroes.gamerguide.ui.gamelists

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.matheusfroes.gamerguide.R
import com.matheusfroes.gamerguide.data.model.GameList
import kotlinx.android.synthetic.main.view_lista.view.*

class ListasAdapter(private val context: Context) : RecyclerView.Adapter<ListasAdapter.ViewHolder>() {
    var lists: List<GameList> = mutableListOf()
    set(value) {
        field = value
        notifyDataSetChanged()
    }
    private var listener: OnListaClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
            ViewHolder(LayoutInflater.from(context).inflate(R.layout.view_lista, parent, false))

    override fun getItemCount() = lists.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val lista = lists[position]

        holder.itemView.tvNome.text = lista.name

        val qtdJogos = lista.games.size

        val string = if (qtdJogos == 0)
            context.getString(R.string.qtd_jogos_0)
        else
            context.resources.getQuantityString(R.plurals.qtd_jogos, qtdJogos, qtdJogos)

        holder.itemView.tvQuantidadeJogos.text = string
    }

    fun setOnListaClickListener(listener: OnListaClickListener) {
        this.listener = listener
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {
            itemView.setOnClickListener {
                val lista = lists[adapterPosition]
                listener?.onListaClick(lista.id)
            }
        }
    }

    interface OnListaClickListener {
        fun onListaClick(listaId: Long)
    }
}