package com.matheusfroes.gamerguide.ui.estatisticas

import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.matheusfroes.gamerguide.R
import com.matheusfroes.gamerguide.data.db.JogosDAO
import kotlinx.android.synthetic.main.activity_estatisticas.*
import kotlinx.android.synthetic.main.toolbar.*
import lecho.lib.hellocharts.model.PieChartData
import lecho.lib.hellocharts.model.SliceValue

class EstatisticasActivity : Fragment() {
    private val jogosDAO: JogosDAO by lazy { JogosDAO(activity) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
            inflater.inflate(R.layout.activity_estatisticas, container, false)


    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.tabLayout?.visibility = View.GONE

        tvQtdJogosNaoTerminados.text = "${jogosDAO.quantidadeJogos(zerado = false)}"
        tvQtdJogosZerados.text = "${jogosDAO.quantidadeJogos(zerado = true)}"
        tvQtdHorasJogadas.text = "${jogosDAO.quantidadeHorasJogadas()}"

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

        chart.pieChartData = pieData
        chart.isChartRotationEnabled = false
        chart.animation = null
    }
}