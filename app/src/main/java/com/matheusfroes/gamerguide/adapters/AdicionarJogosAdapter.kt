package com.matheusfroes.gamerguide.adapters

import android.content.Context
import android.content.Intent
import android.support.v7.widget.PopupMenu
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.matheusfroes.gamerguide.DialogDetalhesJogo
import com.matheusfroes.gamerguide.R
import com.matheusfroes.gamerguide.activities.DetalhesJogoActivity
import com.matheusfroes.gamerguide.models.Jogo
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.view_jogo_pesquisa.view.*
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by matheusfroes on 21/09/2017.
 */
class AdicionarJogosAdapter(private val context: Context) : RecyclerView.Adapter<AdicionarJogosAdapter.ViewHolder>() {
    private var jogos: MutableList<Jogo> = mutableListOf()

    fun preencherLista(jogos: List<Jogo>) {
        this.jogos = jogos.toMutableList()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val view = View.inflate(context, R.layout.view_jogo_pesquisa, null)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val jogo = jogos[position]

        holder.itemView.setOnClickListener {
            dialogDetalhesJogo(jogo)
        }

        holder.itemView.tvNomeJogo.text = jogo.nome
        val dataLancamento = SimpleDateFormat("dd/MM/yyyy", Locale("pt", "BR")).format(jogo.dataLancamento)

        holder.itemView.tvDataLancamento.text = dataLancamento

        if (jogo.imageCapa.isEmpty()) {
            holder.itemView.ivCapaJogo.visibility = View.GONE
        } else {
            Picasso
                    .with(context)
                    .load(jogo.imageCapa.replace("t_thumb", "t_cover_big"))
                    .fit()
                    .into(holder.itemView.ivCapaJogo)
        }
    }

    private fun showPopup(v: View) {
        val popup = PopupMenu(context, v)
        val inflater = popup.menuInflater
        inflater.inflate(R.menu.popup_jogos, popup.menu)
        popup.show()
    }

    private fun dialogDetalhesJogo(jogo: Jogo) {
        val dialog = DialogDetalhesJogo(context, jogo)
                .setPositiveButton("Adicionar", null)
                .setNegativeButton("Detalhes") { _, _ ->
                    context.startActivity(Intent(context, DetalhesJogoActivity::class.java))
                }
                .create()

        dialog.show()
    }


    override fun getItemCount() = jogos.size

    inner class ViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView)
}