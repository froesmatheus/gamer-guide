package com.matheusfroes.gamerguide.fragments

import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.matheusfroes.gamerguide.R
import com.matheusfroes.gamerguide.db.JogosDAO
import kotlinx.android.synthetic.main.fragment_estatisticas.view.*
import kotlinx.android.synthetic.main.toolbar.*
import lecho.lib.hellocharts.model.PieChartData
import lecho.lib.hellocharts.model.SliceValue

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


        val itens = mutableListOf<SliceValue>()

        val colors = listOf(
                "#F44336",
                "#2196F3",
                "#4CAF50",
                "#FFC107",
                "#795548"
        )

        generosMaisJogados.forEachIndexed { index, genero ->
            val sliceValue = SliceValue(genero.second.toFloat(), Color.parseColor(colors[index]))
            sliceValue.setLabel("${genero.first} (${sliceValue.value.toInt()})")
            itens.add(sliceValue)
        }

        val pieData = PieChartData(itens)

        pieData.setHasLabels(true)

        view.chart.pieChartData = pieData
        view.chart.isChartRotationEnabled = false
        view.chart.animation = null

        return view
    }
}