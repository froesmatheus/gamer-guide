package com.matheusfroes.gamerguide.ui.addgames

import android.content.Intent
import android.support.v7.widget.PopupMenu
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.matheusfroes.gamerguide.R
import com.matheusfroes.gamerguide.data.model.Game
import com.matheusfroes.gamerguide.extra.formatarData
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.view_jogo_pesquisa.view.*


class AddGamesAdapter : RecyclerView.Adapter<AddGamesAdapter.ViewHolder>() {
    private var jogos: MutableList<Game> = mutableListOf()
        set(value) {
            field.addAll(value)
            notifyDataSetChanged()
        }

    var onPopupMenuClick: ((action: String, jogo: Game) -> Any)? = null
    var gameDetailsClick: ((game: Game) -> Any)? = null

    fun preencherLista(jogos: List<Game>) {
        this.jogos.addAll(jogos)
        notifyDataSetChanged()
    }


    fun limparLista() {
        jogos.clear()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = View.inflate(parent.context, R.layout.view_jogo_pesquisa, null)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val jogo = jogos[position]

        holder.bind(jogo)

    }

    private fun showPopup(v: View, jogo: Game) {
        val popup = PopupMenu(v.context, v)
        val inflater = popup.menuInflater
        inflater.inflate(R.menu.menu_adicionar_jogos, popup.menu)
        popup.show()

        popup.setOnMenuItemClickListener {
            onPopupMenuClick?.invoke("adicionar_a_lista", jogo)
            true
        }
    }

    override fun getItemCount() = jogos.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var capaVisivel = true

        fun bind(jogo: Game) {
            itemView.tvNomeJogo.text = jogo.name
            itemView.textview.text = jogo.releaseDate?.formatarData("dd/MM/yyyy")
            itemView.tvPlataformas.text = jogo.platforms.joinToString()
            itemView.ivCapaJogo.contentDescription = itemView.context.getString(R.string.content_description_capa_jogo, jogo.name)

            capaVisivel = !jogo.coverImage.isEmpty()


            if (capaVisivel) {
                itemView.ivCapaJogo.visibility = View.VISIBLE
                Picasso
                        .with(itemView.context)
                        .load(jogo.coverImage.replace("t_thumb", "t_cover_big"))
                        .fit()
                        .into(itemView.ivCapaJogo)
            } else {
                itemView.ivCapaJogo.visibility = View.GONE
            }
        }

        init {
            itemView.ivOverflow.setOnClickListener {
                val jogo = jogos[adapterPosition]
                showPopup(itemView.ivOverflow, jogo)
            }

            itemView.setOnClickListener {
                val jogo = jogos[adapterPosition]
                gameDetailsClick?.invoke(jogo)
            }

            itemView.ivCapaJogo.setOnClickListener {
                val jogo = jogos[adapterPosition]
                val intent = Intent(itemView.context, GameCoverActivity::class.java)
                intent.putExtra("url_imagem", jogo.coverImage)
                itemView.context.startActivity(intent)
            }
        }
    }
}