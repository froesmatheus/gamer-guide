package com.matheusfroes.gamerguide.ui.meusjogos

import android.content.Context
import android.content.Intent
import android.support.v7.widget.PopupMenu
import android.support.v7.widget.RecyclerView
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import com.matheusfroes.gamerguide.R
import com.matheusfroes.gamerguide.data.model.Game
import com.matheusfroes.gamerguide.ui.detalhesjogo.DetalhesJogoActivity
import com.squareup.picasso.Picasso


class MeusJogosAdapter(private val context: Context) : RecyclerView.Adapter<MeusJogosAdapter.ViewHolder>() {
    private var jogos = listOf<Game>()
    private var listener: OnMenuOverflowClickListener? = null


    fun preencherLista(jogos: List<Game>) {
        this.jogos = jogos
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = View.inflate(context, R.layout.view_jogo, null)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val jogo = jogos[position]

        holder.itemView.tvNomeJogo.text = jogo.name
        holder.itemView.tvPorcentagemProgresso.text = "${jogo.progress.percentage}%"
        holder.itemView.tvHorasJogadas.text = "${jogo.progress.hoursPlayed} horas"
        holder.itemView.ivCapaJogo.contentDescription = context.getString(R.string.content_description_capa_jogo, jogo.name)

        Picasso
                .with(context)
                .load(jogo.coverImage.replace("t_thumb", "t_cover_big"))
                .fit()
                .into(holder.itemView.ivCapaJogo)
    }

    private fun showPopup(v: View, jogo: Game) {
        val popup = PopupMenu(context, v)
        val inflater = popup.menuInflater
        if (jogo.progress.beaten) {
            inflater.inflate(R.menu.popup_jogos_zerados, popup.menu)
        } else {
            inflater.inflate(R.menu.popup_jogos_nao_terminados, popup.menu)
        }
        popup.show()

        popup.setOnMenuItemClickListener {
            listener?.onMenuItemClick(it, jogo.id)!!
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

                val intent = Intent(context, DetalhesJogoActivity::class.java)
                intent.putExtra("id_jogo", jogo.id)
                context.startActivity(intent)
            }

            itemView.ivOverflowMenu.setOnClickListener {
                val jogo = jogos[adapterPosition]

                showPopup(itemView.ivOverflowMenu, jogo)
            }
        }
    }

    interface OnMenuOverflowClickListener {
        fun onMenuItemClick(menu: MenuItem, jogoId: Long)
    }
}