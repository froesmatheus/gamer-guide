package com.matheusfroes.gamerguide

import android.content.Context
import android.support.v7.app.AlertDialog
import android.view.View
import com.matheusfroes.gamerguide.models.Jogo
import kotlinx.android.synthetic.main.dialog_detalhes_jogo.view.*


/**
 * Created by matheusfroes on 25/09/2017.
 */
class DialogDetalhesJogo(context: Context, private val jogo: Jogo) : AlertDialog.Builder(context) {

    init {
        val view = View.inflate(context, R.layout.dialog_detalhes_jogo, null)
        setView(view)

        carregarJogo(view)
    }

    private fun carregarJogo(view: View) {
        view.tvNomeJogo.text = jogo.nome
        view.tvNomeProdutura.text = jogo.produtora
        view.tvNomePublicadora.text = jogo.publicadora
        view.tvDescricao.text = jogo.descricao
        view.tvGenero.text = jogo.genero
        view.tvDataLancamento.text = jogo.dataLancamento.toString()
    }
}