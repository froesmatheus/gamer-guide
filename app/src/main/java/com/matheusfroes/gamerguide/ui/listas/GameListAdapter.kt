package com.matheusfroes.gamerguide.ui.listas

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.matheusfroes.gamerguide.R
import com.matheusfroes.gamerguide.data.model.GameList
import kotlinx.android.synthetic.main.view_lista.view.*

class GameListAdapter : RecyclerView.Adapter<GameListAdapter.ViewHolder>() {
    var gameLists: List<GameList> = mutableListOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    private var listener: ((Long) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
            ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.view_lista, parent, false))

    override fun getItemCount() = gameLists.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val lista = gameLists[position]

        holder.bind(lista)

    }

    fun gameListClick(listener: (Long) -> Unit) {
        this.listener = listener
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(lista: GameList) {
            itemView.tvNome.text = lista.name

            val qtdJogos = lista.games.size


            val string = if (qtdJogos == 0)
                itemView.context.getString(R.string.qtd_jogos_0)
            else
                itemView.context.resources.getQuantityString(R.plurals.qtd_jogos, qtdJogos, qtdJogos)

            itemView.tvQuantidadeJogos.text = string
        }

        init {
            itemView.setOnClickListener {
                val lista = gameLists[adapterPosition]
                listener?.invoke(lista.id)
            }
        }
    }
}