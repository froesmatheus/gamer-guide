package com.matheusfroes.gamerguide.ui.statistics

import android.arch.lifecycle.ViewModelProviders
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.matheusfroes.gamerguide.MainActivity
import com.matheusfroes.gamerguide.R
import com.matheusfroes.gamerguide.ViewModelFactory
import com.matheusfroes.gamerguide.activity
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.activity_estatisticas.*
import lecho.lib.hellocharts.model.PieChartData
import lecho.lib.hellocharts.model.SliceValue
import javax.inject.Inject

class StatisticsFragment : DaggerFragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var viewModel: StatisticsViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
            inflater.inflate(R.layout.activity_estatisticas, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        viewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(StatisticsViewModel::class.java)


        val beatenGames = viewModel.getGameCountByProgressStatus(beaten = true)
        val notBeatenGames = viewModel.getGameCountByProgressStatus(beaten = false)
        val totalHoursPlayed = viewModel.getTotalHoursPlayed()

        tvQtdJogosNaoTerminados.text = "$notBeatenGames"
        tvQtdJogosZerados.text = "$beatenGames"
        tvQtdHorasJogadas.text = "$totalHoursPlayed"

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
    }

    override fun onResume() {
        super.onResume()

        (activity() as MainActivity).currentScreen(R.id.menu_estatisticas)
    }
}