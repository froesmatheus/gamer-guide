package com.matheusfroes.gamerguide.ui.gamelists

import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.matheusfroes.gamerguide.R
import com.matheusfroes.gamerguide.data.models.Jogo
import com.matheusfroes.gamerguide.formatarData
import com.matheusfroes.gamerguide.ui.detalhesjogo.DetalhesJogoActivity
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.view_jogo_lista.view.*

class JogosListaAdapter() : RecyclerView.Adapter<JogosListaAdapter.ViewHolder>() {
    private var jogos: MutableList<Jogo> = mutableListOf()

    fun preencherLista(jogos: List<Jogo>) {
        this.jogos = jogos.toMutableList()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = View.inflate(parent.context, R.layout.view_jogo_lista, null)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val jogo = jogos[position]

        holder.bind(jogo)
    }

    override fun getItemCount() = jogos.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var capaVisivel = true

        fun bind(jogo: Jogo) {
            itemView.tvNomeJogo.text = jogo.nome
            itemView.textview.text = jogo.dataLancamento.formatarData("dd/MM/yyyy")
            itemView.tvPlataformas.text = jogo.plataformas.joinToString()
            itemView.ivCapaJogo.contentDescription = itemView.context.getString(R.string.content_description_capa_jogo, jogo.nome)

            if (jogo.imageCapa.isEmpty()) {
                capaVisivel = false
                itemView.ivCapaJogo.visibility = if (capaVisivel) View.VISIBLE else View.GONE
            } else {
                capaVisivel = true
                itemView.ivCapaJogo.visibility = if (capaVisivel) View.VISIBLE else View.GONE

                Picasso.with(itemView.context)
                        .load(jogo.imageCapa.replace("t_thumb", "t_cover_big"))
                        .fit()
                        .into(itemView.ivCapaJogo)
            }
        }


        init {
            itemView.setOnClickListener {
                val jogo = jogos[adapterPosition]

                val intent = Intent(itemView.context, DetalhesJogoActivity::class.java)
                intent.putExtra("id_jogo", jogo.id)
                itemView.context.startActivity(intent)
            }
        }
    }
}