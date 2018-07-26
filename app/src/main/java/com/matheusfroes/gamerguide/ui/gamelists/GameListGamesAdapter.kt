package com.matheusfroes.gamerguide.ui.gamelists

import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.matheusfroes.gamerguide.R
import com.matheusfroes.gamerguide.data.model.Game
import com.matheusfroes.gamerguide.formatarData
import com.matheusfroes.gamerguide.ui.detalhesjogo.DetalhesJogoActivity
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.view_jogo_lista.view.*

class GameListGamesAdapter : RecyclerView.Adapter<GameListGamesAdapter.ViewHolder>() {
    var games: List<Game> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = View.inflate(parent.context, R.layout.view_jogo_lista, null)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val jogo = games[position]

        holder.bind(jogo)
    }

    override fun getItemCount() = games.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var capaVisivel = true

        fun bind(jogo: Game) {
            itemView.tvNomeJogo.text = jogo.name
            itemView.textview.text = jogo.releaseDate.formatarData("dd/MM/yyyy")
            itemView.tvPlataformas.text = jogo.platforms.joinToString()
            itemView.ivCapaJogo.contentDescription = itemView.context.getString(R.string.content_description_capa_jogo, jogo.name)

            if (jogo.coverImage.isEmpty()) {
                capaVisivel = false
                itemView.ivCapaJogo.visibility = if (capaVisivel) View.VISIBLE else View.GONE
            } else {
                capaVisivel = true
                itemView.ivCapaJogo.visibility = if (capaVisivel) View.VISIBLE else View.GONE

                Picasso.with(itemView.context)
                        .load(jogo.coverImage.replace("t_thumb", "t_cover_big"))
                        .fit()
                        .into(itemView.ivCapaJogo)
            }
        }


        init {
            itemView.setOnClickListener {
                val jogo = games[adapterPosition]

                val intent = Intent(itemView.context, DetalhesJogoActivity::class.java)
                intent.putExtra("id_jogo", jogo.id)
                itemView.context.startActivity(intent)
            }
        }
    }
}