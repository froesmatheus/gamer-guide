package com.matheusfroes.gamerguide.ui.gameprogressdialog

import android.arch.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.matheusfroes.gamerguide.R
import com.matheusfroes.gamerguide.extra.appInjector
import com.matheusfroes.gamerguide.data.model.Game
import com.matheusfroes.gamerguide.extra.toast
import com.matheusfroes.gamerguide.extra.viewModelProvider
import com.matheusfroes.gamerguide.widget.CustomDialogFragment
import kotlinx.android.synthetic.main.gameprogress_dialog_view.*
import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar
import javax.inject.Inject


class GameProgressDialog : CustomDialogFragment() {
    companion object {
        private const val GAME_PARAMETER = "game"

        fun newInstance(game: Game) = GameProgressDialog().apply {
            arguments = Bundle().apply {
                putSerializable(GAME_PARAMETER, game)
            }
        }
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: GameProgressViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
            inflater.inflate(R.layout.gameprogress_dialog_view, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        appInjector.inject(this)

        viewModel = viewModelProvider(viewModelFactory)

        val game = arguments?.getSerializable(GAME_PARAMETER) as Game

        btnCancel.setOnClickListener { dismiss() }

        btnUpdateProgress.setOnClickListener { updateProgress(game) }


        sbProgresso.setOnProgressChangeListener(object : DiscreteSeekBar.OnProgressChangeListener {
            override fun onProgressChanged(seekBar: DiscreteSeekBar?, value: Int, fromUser: Boolean) {
                tvPorcentagemProgresso.text = "$value%"
                chkJogoZerado.isChecked = value == 100
            }

            override fun onStartTrackingTouch(seekBar: DiscreteSeekBar?) {}

            override fun onStopTrackingTouch(seekBar: DiscreteSeekBar?) {}

        })

        etHorasJogadas.setText("${game.progress.hoursPlayed}")
        sbProgresso.progress = game.progress.percentage
        chkJogoZerado.isChecked = game.progress.beaten
    }

    private fun updateProgress(game: Game) {
        var horasJogadasStr = etHorasJogadas.text.toString().trim()
        horasJogadasStr = if (horasJogadasStr.isEmpty()) "0" else horasJogadasStr

        game.progress.hoursPlayed = Integer.parseInt(horasJogadasStr)
        game.progress.percentage = sbProgresso.progress
        game.progress.beaten = chkJogoZerado.isChecked

        viewModel.updateGameProgress(game)

        toast(R.string.progresso_atualizado)
        dismiss()
    }
}