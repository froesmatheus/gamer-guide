package com.matheusfroes.gamerguide.ui.statistics

import android.arch.lifecycle.ViewModelProvider
import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.matheusfroes.gamerguide.R
import com.matheusfroes.gamerguide.extra.appCompatActivity
import com.matheusfroes.gamerguide.extra.appInjector
import com.matheusfroes.gamerguide.extra.viewModelProvider
import kotlinx.android.synthetic.main.activity_estatisticas.*
import kotlinx.android.synthetic.main.toolbar.*
import kotlinx.android.synthetic.main.toolbar.view.*
import lecho.lib.hellocharts.model.PieChartData
import lecho.lib.hellocharts.model.SliceValue
import javax.inject.Inject

class StatisticsFragment : Fragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: StatisticsViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
            inflater.inflate(R.layout.activity_estatisticas, container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        appInjector.inject(this)
        setupToolbar()

        viewModel = viewModelProvider(viewModelFactory)

        tvQtdJogosNaoTerminados.text = "${viewModel.getGameCount(beaten = false)}"
        tvQtdJogosZerados.text = "${viewModel.getGameCount(beaten = true)}"
        tvQtdHorasJogadas.text = "${viewModel.getTotalHoursPlayed()}"

        val generosMaisJogados = viewModel.getMostPlayedGenres()

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

        if (generosMaisJogados.isEmpty()) {
            emptyViewStatistics.visibility = View.VISIBLE
        }
    }

    private fun setupToolbar() {
        appCompatActivity.setSupportActionBar(toolbar)
        appCompatActivity.supportActionBar?.setDisplayShowTitleEnabled(false)
        toolbar.toolbarTitle.text = "Estatísticas"
    }
}