package com.matheusfroes.gamerguide.ui.calendar

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.matheusfroes.gamerguide.R
import com.matheusfroes.gamerguide.formatarData
import com.matheusfroes.gamerguide.network.data.*
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.view_jogo_lancamento2.view.*

class LancamentosAdapter(private val context: Context) : RecyclerView.Adapter<LancamentosAdapter.ViewHolder>() {
    var lancamentos = mutableListOf<Lancamento>()

    fun preencherLista(lancamentos: MutableList<Lancamento>) {
        this.lancamentos.addAll(lancamentos)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.view_jogo_lancamento2, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val lancamento = lancamentos[position]

        holder.itemView.tvNomeJogo.text = lancamento.game.nome
        holder.itemView.tvDataLancamento.text = lancamento.date.formatarData("dd/MM/yyyy")
        holder.itemView.tvPlataforma.text = lancamento.platform.toString()
        holder.itemView.tvRegiao.text = lancamento.region

        val imagemRegiao = when (lancamento.region) {
            REGIAO_AMERICA_NORTE -> R.drawable.regiao_america_norte
            REGIAO_EUROPA -> R.drawable.regiao_europa
            REGIAO_AUSTRALIA -> R.drawable.regiao_australia
            REGIAO_NOVA_ZELANDIA -> R.drawable.regiao_nova_zelandia
            REGIAO_JAPAO -> R.drawable.regiao_japao
            REGIAO_CHINA -> R.drawable.regiao_china
            REGIAO_ASIA -> R.drawable.regiao_asia
            REGIAO_MUNDIAL -> R.drawable.regiao_mundial
            else -> R.drawable.regiao_mundial
        }

        holder.itemView.ivRegiao.setImageResource(imagemRegiao)



        holder.capaVisivel = !lancamento.game.cover.isEmpty()

        if (holder.capaVisivel) {
            holder.itemView.ivCapaJogo.visibility = View.VISIBLE
            Picasso
                    .with(context)
                    .load(lancamento.game.cover.replace("t_thumb", "t_screenshot_big"))
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