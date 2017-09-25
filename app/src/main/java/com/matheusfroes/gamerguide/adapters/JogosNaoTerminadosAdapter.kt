package com.matheusfroes.gamerguide.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.matheusfroes.gamerguide.DialogDetalhesJogo
import com.matheusfroes.gamerguide.R
import com.matheusfroes.gamerguide.models.Jogo
import java.util.*

/**
 * Created by matheusfroes on 21/09/2017.
 */
class JogosNaoTerminadosAdapter(private val context: Context) : RecyclerView.Adapter<JogosNaoTerminadosAdapter.ViewHolder>() {
    private val jogos = listOf("A", "B", "C", "D", "E", "F", "G", "C", "D", "E", "C", "D", "E", "C", "D", "E")

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val jogo = Jogo("FIFA 18", "FIFA é uma merda todo ano, o ÚNICO bom foi o 14 que tinha Mandzukic robado de cabeça",
                "EA (Pior empresa dos EUA)", "EA (Pior empresa dos EUA)", "Esporte", Calendar.getInstance().time)
        holder.itemView.setOnClickListener {
            //context.startActivity(Intent(context, DetalhesJogoActivity::class.java))
            dialogDetalhesJogo(jogo)
        }
    }

    private fun dialogDetalhesJogo(jogo: Jogo) {
        val dialog = DialogDetalhesJogo(context, jogo)
                .setPositiveButton("Adicionar Jogo", null)
                .setNegativeButton("Mostrar Detalhes", null)
                .create()

        dialog.show()
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val view = View.inflate(context, R.layout.view_jogo, null)
        return ViewHolder(view)
    }

    override fun getItemCount() = jogos.size

    inner class ViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView)
}