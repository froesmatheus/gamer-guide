package com.matheusfroes.gamerguide.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.matheusfroes.gamerguide.R
import com.matheusfroes.gamerguide.db.JogosDAO
import kotlinx.android.synthetic.main.fragment_estatisticas.view.*
import kotlinx.android.synthetic.main.toolbar.*

/**
 * Created by matheusfroes on 04/10/2017.
 */
class EstatisticasFragment : Fragment() {
    private val jogosDAO: JogosDAO by lazy { JogosDAO(context) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_estatisticas, container, false)
        activity.tabLayout.visibility = View.GONE

        view.tvQtdJogosNaoTerminados.text = "${jogosDAO.quantidadeJogos(zerado = false)}"
        view.tvQtdJogosZerados.text = "${jogosDAO.quantidadeJogos(zerado = true)}"
        view.tvQtdHorasJogadas.text = "${jogosDAO.quantidadeHorasJogadas()}"

        val generosMaisJogados = jogosDAO.obterGenerosMaisJogados()

        generosMaisJogados.forEachIndexed { index, genero ->

        }

        return view
    }
}