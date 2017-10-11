package com.matheusfroes.gamerguide.adapters

import android.content.Context
import android.content.Intent
import android.support.v7.widget.PopupMenu
import android.support.v7.widget.RecyclerView
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import com.matheusfroes.gamerguide.R
import com.matheusfroes.gamerguide.activities.DetalhesJogoActivity
import com.matheusfroes.gamerguide.models.Jogo
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.view_jogo.view.*


/**
 * Created by matheusfroes on 21/09/2017.
 */
class MeusJogosAdapter(private val context: Context) : RecyclerView.Adapter<MeusJogosAdapter.ViewHolder>() {
    private var jogos = listOf<Jogo>()
    private var listener: OnMenuOverflowClickListener? = null


    fun preencherLista(jogos: List<Jogo>) {
        this.jogos = jogos
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val view = View.inflate(context, R.layout.view_jogo, null)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val jogo = jogos[position]

        holder.itemView.setOnClickListener {
            val intent = Intent(context, DetalhesJogoActivity::class.java)
            intent.putExtra("id_jogo", jogo.id)
            context.startActivity(intent)
        }

        holder.itemView.tvNomeJogo.text = jogo.nome
        holder.itemView.tvPorcentagemProgresso.text = "${jogo.progresso.progressoPerc}%"
        holder.itemView.tvHorasJogadas.text = "${jogo.progresso.horasJogadas} horas"

        Picasso
                .with(context)
                .load(jogo.imageCapa.replace("t_thumb", "t_cover_big"))
                .fit()
                .into(holder.itemView.ivCapaJogo)

        holder.itemView.ivOverflowMenu.setOnClickListener {
            showPopup(holder.itemView.ivOverflowMenu, jogo.id)
        }
    }

    private fun showPopup(v: View, itemId: Long) {
        val popup = PopupMenu(context, v)
        val inflater = popup.menuInflater
        inflater.inflate(R.menu.popup_jogos, popup.menu)
        popup.show()

        popup.setOnMenuItemClickListener {
            listener?.onMenuItemClick(it, itemId)!!
            true
        }
    }

    fun setOnMenuItemClickListener(listener: OnMenuOverflowClickListener) {
        this.listener = listener
    }

    override fun getItemCount() = jogos.size

    inner class ViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView)

    interface OnMenuOverflowClickListener {
        fun onMenuItemClick(menu: MenuItem, jogoId: Long)
    }
}