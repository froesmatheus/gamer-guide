package com.matheusfroes.gamerguide

import android.content.Context
import android.support.v7.app.AlertDialog
import android.view.View
import com.matheusfroes.gamerguide.models.Jogo
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.dialog_detalhes_jogo.view.*
import java.text.SimpleDateFormat
import java.util.*


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
        view.tvDesenvolvedores.text = jogo.desenvolvedoras
        view.tvPublicadora.text = jogo.publicadoras
        view.tvGenero.text = jogo.generos

        val dataLancamento = SimpleDateFormat("dd 'de' MMMM 'de' yyyy", Locale("pt", "BR")).format(jogo.dataLancamento)


        view.tvDataLancamento.text = dataLancamento

        Picasso.with(context).load(jogo.imageCapa.replace("t_thumb", "t_cover_big")).fit()
                .into(view.ivCapaJogo)
    }
}