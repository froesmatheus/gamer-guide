package com.matheusfroes.gamerguide.adapters

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.matheusfroes.gamerguide.R
import com.matheusfroes.gamerguide.activities.DetalhesJogoActivity
import com.matheusfroes.gamerguide.extra.DialogDetalhesJogo
import com.matheusfroes.gamerguide.formatarData
import com.matheusfroes.gamerguide.models.Jogo
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.view_jogo_pesquisa.view.*

/**
 * Created by matheusfroes on 21/09/2017.
 */
class AdicionarJogosAdapter(private val context: Context) : RecyclerView.Adapter<AdicionarJogosAdapter.ViewHolder>() {
    private var jogos: List<Jogo> = listOf()

    fun preencherLista(jogos: List<Jogo>) {
        this.jogos = jogos
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val view = View.inflate(context, R.layout.view_jogo_pesquisa, null)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val jogo = jogos[position]

        holder.itemView.setOnClickListener { dialogDetalhesJogo(jogo) }

        holder.itemView.tvNomeJogo.text = jogo.nome
        holder.itemView.tvDataLancamento.text = jogo.dataLancamento.formatarData("dd/MM/yyyy")
        holder.itemView.tvPlataformas.text = jogo.plataformas.joinToString()

        holder.setIsRecyclable(false)

        if (jogo.imageCapa.isEmpty()) {
            holder.capaVisivel = false
            holder.itemView.ivCapaJogo.visibility = if (holder.capaVisivel) View.VISIBLE else View.GONE
        } else {
            holder.capaVisivel = true
            holder.itemView.ivCapaJogo.visibility = if (holder.capaVisivel) View.VISIBLE else View.GONE

            Picasso
                    .with(context)
                    .load(jogo.imageCapa.replace("t_thumb", "t_cover_big"))
                    .fit()
                    .into(holder.itemView.ivCapaJogo)
        }
    }

    private fun dialogDetalhesJogo(jogo: Jogo) {
        val dialog = DialogDetalhesJogo(context, jogo)
                .setPositiveButton("Adicionar", null)
                .setNegativeButton("Detalhes") { _, _ ->
                    val intent = Intent(context, DetalhesJogoActivity::class.java)
                    intent.putExtra("tela_origem", "tela_adicionar")
                    intent.putExtra("jogo", jogo)
                    context.startActivity(intent)
                }
                .create()

        dialog.show()
    }

    override fun getItemCount() = jogos.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var capaVisivel = true
    }
}