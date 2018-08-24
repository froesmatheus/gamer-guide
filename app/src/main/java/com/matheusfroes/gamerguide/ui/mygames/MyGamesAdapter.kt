package com.matheusfroes.gamerguide.ui.mygames

import android.content.Intent
import android.support.v7.widget.PopupMenu
import android.support.v7.widget.RecyclerView
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import com.matheusfroes.gamerguide.R
import com.matheusfroes.gamerguide.data.model.Game
import com.matheusfroes.gamerguide.ui.gamedetails.GameDetailsActivity
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.view_jogo.view.*


class MyGamesAdapter : RecyclerView.Adapter<MyGamesAdapter.ViewHolder>() {
    var jogos = listOf<Game>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    private var listener: OnMenuOverflowClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = View.inflate(parent.context, R.layout.view_jogo, null)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val jogo = jogos[position]

        holder.bind(jogo)
    }

    private fun showPopup(v: View, jogo: Game) {
        val popup = PopupMenu(v.context, v)
        val inflater = popup.menuInflater
        if (jogo.progress.beaten) {
            inflater.inflate(R.menu.popup_jogos_zerados, popup.menu)
        } else {
            inflater.inflate(R.menu.popup_jogos_nao_terminados, popup.menu)
        }
        popup.show()

        popup.setOnMenuItemClickListener {
            listener?.onMenuItemClick(it, jogo)!!
            true
        }
    }

    fun setOnMenuItemClickListener(listener: OnMenuOverflowClickListener) {
        this.listener = listener
    }

    override fun getItemCount() = jogos.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        init {
            itemView.setOnClickListener {
                val jogo = jogos[adapterPosition]

                val intent = Intent(itemView.context, GameDetailsActivity::class.java)
                intent.putExtra("id_jogo", jogo.id)
                itemView.context.startActivity(intent)
            }

            itemView.ivOverflowMenu.setOnClickListener {
                val jogo = jogos[adapterPosition]

                showPopup(itemView.ivOverflowMenu, jogo)
            }
        }

        fun bind(jogo: Game) {
            itemView.tvNomeJogo.text = jogo.name
            itemView.tvPorcentagemProgresso.text = "${jogo.progress.percentage}%"
            itemView.tvHorasJogadas.text = "${jogo.progress.hoursPlayed} horas"
            itemView.ivCapaJogo.contentDescription = itemView.context.getString(R.string.content_description_capa_jogo, jogo.name)

            if (jogo.coverImage.isNotEmpty()) {
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
    }

    interface OnMenuOverflowClickListener {
        fun onMenuItemClick(menu: MenuItem, game: Game)
    }
}