package com.matheusfroes.gamerguide.fragments

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.matheusfroes.gamerguide.R
import com.matheusfroes.gamerguide.activities.DetalhesJogoViewModel
import com.matheusfroes.gamerguide.formatarData
import com.matheusfroes.gamerguide.models.Jogo
import kotlinx.android.synthetic.main.fragment_informacoes_gerais.view.*

/**
 * Created by matheus_froes on 26/09/2017.
 */
class InformacoesGeraisJogoFragment : Fragment() {
    private val viewModel: DetalhesJogoViewModel by lazy {
        ViewModelProviders.of(activity).get(DetalhesJogoViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_informacoes_gerais, container, false)

        viewModel.jogo.observe(this, Observer { jogo ->
            preencherDadosJogo(view, jogo)
        })

        return view
    }

    private fun preencherDadosJogo(view: View, jogo: Jogo?) {
        view.tvNomeJogo.text = jogo?.nome
        view.tvNomeDesenvolvedores.text = jogo?.desenvolvedores
        view.tvNomePublicadora.text = jogo?.publicadoras
        view.tvGenero.text = jogo?.generos
        view.tvDataLancamento.text = jogo?.dataLancamento?.formatarData("dd 'de' MMMM 'de' yyyy")
        view.tvDescricao.text = jogo?.descricao
        view.tvPlataformas.text = jogo?.plataformas?.joinToString()

        if (jogo?.timeToBeat != null) {
            if (jogo.timeToBeat.hastly == 0L) {
                view.tituloTTBSpeedrun.visibility = View.GONE
                view.tvTTBSpeedrun.visibility = View.GONE
            }
            if (jogo.timeToBeat.normally == 0L) {
                view.tituloTTBModoHistoria.visibility = View.GONE
                view.tvTTBModoHistoria.visibility = View.GONE
            }
            if (jogo.timeToBeat.completely == 0L) {
                view.tituloTTB100Perc.visibility = View.GONE
                view.tvTTB100Perc.visibility = View.GONE
            }
            view.tvTTBSpeedrun.text = "${(jogo.timeToBeat.hastly.div(3600))} horas"
            view.tvTTBModoHistoria.text = "${(jogo.timeToBeat.normally.div(3600))} horas"
            view.tvTTB100Perc.text = "${(jogo.timeToBeat.completely.div(3600))} horas"
        } else {
            view.cvCardTimeToBeat.visibility = View.GONE
        }
    }
}