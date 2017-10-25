package com.matheusfroes.gamerguide.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.matheusfroes.gamerguide.R
import com.matheusfroes.gamerguide.formatarData
import com.matheusfroes.gamerguide.models.Lancamento
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.view_jogo_lancamento.view.*

/**
 * Created by matheusfroes on 21/09/2017.
 */
class LancamentosAdapter(private val context: Context) : RecyclerView.Adapter<LancamentosAdapter.ViewHolder>() {
    private var lancamentos = mutableListOf<Lancamento>()

    fun preencherLista(lancamentos: MutableList<Lancamento>) {
        this.lancamentos.addAll(lancamentos)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val view = View.inflate(context, R.layout.view_jogo_lancamento, null)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val lancamento = lancamentos[position]

        holder.itemView.tvNomeJogo.text = lancamento.game.nome
        holder.itemView.tvDataLancamento.text = lancamento.date.formatarData("dd/MM/yyyy")
        holder.itemView.tvPlataforma.text = lancamento.platform.toString()

        holder.capaVisivel = !lancamento.game.cover.isEmpty()

        if (holder.capaVisivel) {
            holder.itemView.ivCapaJogo.visibility = View.VISIBLE
            Picasso
                    .with(context)
                    .load(lancamento.game.cover.replace("t_thumb", "t_cover_big"))
                    .fit()
                    .into(holder.itemView.ivCapaJogo)
        } else {
            holder.itemView.ivCapaJogo.visibility = View.GONE
        }

    }

    override fun getItemCount() = lancamentos.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var capaVisivel = true
    }
}