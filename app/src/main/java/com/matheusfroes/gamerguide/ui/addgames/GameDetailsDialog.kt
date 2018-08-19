package com.matheusfroes.gamerguide.ui.addgames

import android.content.Context
import android.support.v7.app.AlertDialog
import android.view.View
import com.matheusfroes.gamerguide.R
import com.matheusfroes.gamerguide.data.model.Game
import com.matheusfroes.gamerguide.formatarData
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.dialog_detalhes_jogo.view.*

class GameDetailsDialog(context: Context, private val jogo: Game) : AlertDialog.Builder(context) {

    init {
        val view = View.inflate(context, R.layout.dialog_detalhes_jogo, null)
        setView(view)

        carregarJogo(view)
    }

    private fun carregarJogo(view: View) {
        view.tvNomeJogo.text = jogo.name
        view.tvDesenvolvedores.text = jogo.developers
        view.tvPublicadora.text = jogo.publishers
        view.tvGenero.text = jogo.genres

        val dataLancamento = jogo.releaseDate?.formatarData("dd 'de' MMMM 'de' yyyy")

        view.tvDataLancamento.text = dataLancamento

        if (jogo.releaseDate == null) {
            view.tvDataLancamento.visibility = View.GONE
            view.tituloDataLancamento.visibility = View.GONE
        }

        if (jogo.developers.isEmpty()) {
            view.tvDesenvolvedores.visibility = View.GONE
            view.tituloDesenvolvedores.visibility = View.GONE
        }

        if (jogo.publishers.isEmpty()) {
            view.tvPublicadora.visibility = View.GONE
            view.tituloPublicadoras.visibility = View.GONE
        }

        if (jogo.genres.isEmpty()) {
            view.tituloGenero.visibility = View.GONE
            view.tvGenero.visibility = View.GONE
        }


        if (jogo.coverImage.isNotEmpty()) {
            Picasso.with(context).load(jogo.coverImage.replace("t_thumb", "t_cover_big")).fit()
                    .into(view.ivCapaJogo)
        } else {
            view.ivCapaJogo.visibility = View.GONE
        }
    }
}