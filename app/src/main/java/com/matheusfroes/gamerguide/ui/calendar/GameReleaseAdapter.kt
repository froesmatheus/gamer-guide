package com.matheusfroes.gamerguide.ui.calendar

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.matheusfroes.gamerguide.R
import com.matheusfroes.gamerguide.data.model.Region
import com.matheusfroes.gamerguide.data.model.Release
import com.matheusfroes.gamerguide.formatarData
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.view_jogo_lancamento2.view.*

class GameReleaseAdapter : RecyclerView.Adapter<GameReleaseAdapter.ViewHolder>() {
    var lancamentos = mutableListOf<Release>()

    fun preencherLista(lancamentos: MutableList<Release>) {
        this.lancamentos.addAll(lancamentos)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.view_jogo_lancamento2, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val lancamento = lancamentos[position]

        holder.bind(lancamento)

    }

    override fun getItemCount() = lancamentos.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var capaVisivel = true

        fun bind(lancamento: Release) {
            itemView.tvNomeJogo.text = lancamento.game.name
            itemView.tvDataLancamento.text = lancamento.date.formatarData("dd/MM/yyyy")
            itemView.tvPlataforma.text = lancamento.platform.toString()
            itemView.tvRegiao.text = lancamento.region.toString()

            val imagemRegiao = when (lancamento.region) {
                Region.EUROPE -> R.drawable.regiao_europa
                Region.NORTH_AMERICA -> R.drawable.regiao_america_norte
                Region.AUSTRALIA -> R.drawable.regiao_australia
                Region.NEW_ZEALAND -> R.drawable.regiao_nova_zelandia
                Region.JAPAN -> R.drawable.regiao_japao
                Region.CHINA -> R.drawable.regiao_china
                Region.ASIA -> R.drawable.regiao_asia
                Region.WORLD -> R.drawable.regiao_mundial
            }

            itemView.ivRegiao.setImageResource(imagemRegiao)

            capaVisivel = !lancamento.game.cover.isEmpty()

            if (capaVisivel) {
                itemView.ivCapaJogo.visibility = View.VISIBLE
                Picasso
                        .with(itemView.context)
                        .load(lancamento.game.cover.replace("t_thumb", "t_screenshot_big"))
                        .fit()
                        .into(itemView.ivCapaJogo)
            } else {
                itemView.ivCapaJogo.visibility = View.GONE
            }
        }
    }
}