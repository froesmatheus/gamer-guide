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
import kotlinx.android.synthetic.main.view_jogo.view.*
import java.util.*


/**
 * Created by matheusfroes on 21/09/2017.
 */
class JogosNaoTerminadosAdapter(private val context: Context) : RecyclerView.Adapter<JogosNaoTerminadosAdapter.ViewHolder>() {
    private val jogos = listOf("A", "B", "C", "D", "E", "F", "G", "C", "D", "E", "C", "D", "E", "C", "D", "E")

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val view = View.inflate(context, R.layout.view_jogo, null)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val jogo = Jogo("FIFA 18", "FIFA é uma merda todo ano, o ÚNICO bom foi o 14 que tinha Mandzukic robado de cabeça",
                "EA (Pior empresa dos EUA)", "EA (Pior empresa dos EUA)", "Esporte", Calendar.getInstance().time)
        holder.itemView.setOnClickListener {
            dialogDetalhesJogo(jogo)
        }

        holder.itemView.ivOverflowMenu.setOnClickListener {
            showPopup(holder.itemView.ivOverflowMenu)
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
                .setNegativeButton("Detalhes") { dialogInterface, i ->
                    context.startActivity(Intent(context, DetalhesJogoActivity::class.java))
                }
                .create()

        dialog.show()
    }


    override fun getItemCount() = jogos.size

    inner class ViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView)
}