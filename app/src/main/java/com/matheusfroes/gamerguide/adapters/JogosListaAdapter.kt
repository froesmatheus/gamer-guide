package com.matheusfroes.gamerguide.adapters

import android.content.Context
import android.content.Intent
import android.support.v7.widget.PopupMenu
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.matheusfroes.gamerguide.R
import com.matheusfroes.gamerguide.activities.DetalhesJogoActivity
import com.matheusfroes.gamerguide.formatarData
import com.matheusfroes.gamerguide.models.Jogo
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.view_jogo_lista.view.*

/**
 * Created by matheusfroes on 21/09/2017.
 */
class JogosListaAdapter(private val context: Context) : RecyclerView.Adapter<JogosListaAdapter.ViewHolder>() {
    private var jogos: MutableList<Jogo> = mutableListOf()

    fun preencherLista(jogos: List<Jogo>) {
        this.jogos = jogos.toMutableList()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val view = View.inflate(context, R.layout.view_jogo_lista, null)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val jogo = jogos[position]

        holder.itemView.tvNomeJogo.text = jogo.nome
        holder.itemView.textview.text = jogo.dataLancamento.formatarData("dd/MM/yyyy")
        holder.itemView.tvPlataformas.text = jogo.plataformas.joinToString()
        holder.itemView.ivCapaJogo.contentDescription = context.getString(R.string.content_description_capa_jogo, jogo.nome)

        if (jogo.imageCapa.isEmpty()) {
            holder.capaVisivel = false
            holder.itemView.ivCapaJogo.visibility = if (holder.capaVisivel) View.VISIBLE else View.GONE
        } else {
            holder.capaVisivel = true
            holder.itemView.ivCapaJogo.visibility = if (holder.capaVisivel) View.VISIBLE else View.GONE

            Picasso.with(context)
                    .load(jogo.imageCapa.replace("t_thumb", "t_cover_big"))
                    .fit()
                    .into(holder.itemView.ivCapaJogo)
        }
    }

    private fun showPopup(v: View) {
        val popup = PopupMenu(context, v)
        val inflater = popup.menuInflater
        inflater.inflate(R.menu.popup_jogos_nao_terminados, popup.menu)
        popup.show()
    }

    override fun getItemCount() = jogos.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var capaVisivel = true

        init {
            itemView.setOnClickListener {
                val jogo = jogos[adapterPosition]

                val intent = Intent(context, DetalhesJogoActivity::class.java)
                intent.putExtra("id_jogo", jogo.id)
                context.startActivity(intent)
            }
        }
    }
}