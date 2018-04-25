package com.matheusfroes.gamerguide.ui.estatisticas

import android.graphics.Color
import android.os.Bundle
import com.matheusfroes.gamerguide.R
import com.matheusfroes.gamerguide.data.db.JogosDAO
import com.matheusfroes.gamerguide.ui.BaseActivityDrawer
import kotlinx.android.synthetic.main.activity_estatisticas.*
import kotlinx.android.synthetic.main.toolbar.*
import lecho.lib.hellocharts.model.PieChartData
import lecho.lib.hellocharts.model.SliceValue

class EstatisticasActivity : BaseActivityDrawer() {
    private val jogosDAO: JogosDAO by lazy { JogosDAO(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_estatisticas)
        setSupportActionBar(toolbar)
        configurarDrawer()
        title = getString(R.string.estatisticas)

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

    override fun onStart() {
        super.onStart()

        setDrawerSelectedItem(BaseActivityDrawer.ESTATISTICAS_IDENTIFIER)
    }
}