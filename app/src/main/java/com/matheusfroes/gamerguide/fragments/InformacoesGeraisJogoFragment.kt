package com.matheusfroes.gamerguide.fragments

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.matheusfroes.gamerguide.R
import com.matheusfroes.gamerguide.activities.DetalhesJogoViewModel
import com.matheusfroes.gamerguide.formatarData
import com.matheusfroes.gamerguide.models.Jogo
import kotlinx.android.synthetic.main.fragment_informacoes_gerais.*
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
        view.textview.text = jogo?.dataLancamento?.formatarData("dd 'de' MMMM 'de' yyyy")
        view.tvDescricao.text = jogo?.descricao
        view.tvPlataformas.text = jogo?.plataformas?.joinToString()
        view.tvGameEngine.text = jogo?.gameEngine

        if (jogo?.descricao.isNullOrEmpty()) {
            view.cvDescricaoJogo.visibility = View.GONE
        }

        if (jogo?.plataformas?.joinToString().isNullOrEmpty()) {
            view.tvTituloPlataformas.visibility = View.GONE
            view.tvPlataformas.visibility = View.GONE
        }

        if (jogo?.gameEngine.isNullOrEmpty()) {
            view.tvTituloGameEngine.visibility = View.GONE
            view.tvGameEngine.visibility = View.GONE
        }

        if (jogo?.desenvolvedores.isNullOrEmpty()) {
            view.tvNomeDesenvolvedores.visibility = View.GONE
            view.tituloDesenvolvedores.visibility = View.GONE
        }

        if (jogo?.publicadoras.isNullOrEmpty()) {
            view.tvNomePublicadora.visibility = View.GONE
            view.tituloPublicadoras.visibility = View.GONE
        }

        if (jogo?.generos.isNullOrEmpty()) {
            view.tvGenero.visibility = View.GONE
            view.tvTituloGenero.visibility = View.GONE
        }

        cvDescricaoJogo.setOnClickListener { dialogDescricao() }

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
            view.tvTTBSpeedrun.text = context.getString(R.string.horas_ttb, jogo.timeToBeat.hastly.div(3600))
            view.tvTTBModoHistoria.text = context.getString(R.string.horas_ttb, jogo.timeToBeat.normally.div(3600))
            view.tvTTB100Perc.text = context.getString(R.string.horas_ttb, jogo.timeToBeat.completely.div(3600))
        } else {
            view.cvCardTimeToBeat.visibility = View.GONE
        }
    }

    private fun dialogDescricao() {
        val dialog = AlertDialog.Builder(activity)
                .setTitle(getString(R.string.descricao))
                .setMessage(viewModel.jogo.value?.descricao)
                .create()

        dialog.show()
    }
}