package com.matheusfroes.gamerguide.ui.listas

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.matheusfroes.gamerguide.R
import com.matheusfroes.gamerguide.data.model.GameList
import kotlinx.android.synthetic.main.view_lista.view.*

class ListasAdapter(private val context: Context) : RecyclerView.Adapter<ListasAdapter.ViewHolder>() {
    private var listas: List<GameList> = mutableListOf()
    private var listener: OnListaClickListener? = null

    fun preencherLista(listas: List<GameList>) {
        this.listas = listas
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
            ViewHolder(LayoutInflater.from(context).inflate(R.layout.view_lista, parent, false))

    override fun getItemCount() = listas.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val lista = listas[position]

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
                val lista = listas[adapterPosition]
                listener?.onListaClick(lista.id)
            }
        }
    }

    interface OnListaClickListener {
        fun onListaClick(listaId: Int)
    }
}