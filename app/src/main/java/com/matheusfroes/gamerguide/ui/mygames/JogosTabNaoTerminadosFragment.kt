package com.matheusfroes.gamerguide.ui.mygames

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import com.matheusfroes.gamerguide.ExcluirJogoEvent
import com.matheusfroes.gamerguide.GerenciarListasEvent
import com.matheusfroes.gamerguide.R
import com.matheusfroes.gamerguide.appInjector
import com.matheusfroes.gamerguide.data.model.Game
import kotlinx.android.synthetic.main.fragment_jogos_nao_terminados.*
import kotlinx.android.synthetic.main.fragment_jogos_nao_terminados.view.*
import kotlinx.android.synthetic.main.fragment_meu_progresso.view.*
import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar
import org.greenrobot.eventbus.EventBus
import org.jetbrains.anko.toast
import javax.inject.Inject

class JogosTabNaoTerminadosFragment : Fragment() {
    val adapter: MyGamesAdapter by lazy { MyGamesAdapter() }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: MyGamesViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_jogos_nao_terminados, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        appInjector.inject(this)
        viewModel = ViewModelProviders.of(requireActivity(), viewModelFactory)[MyGamesViewModel::class.java]

        rvJogosNaoTerminados.rvJogosNaoTerminados.layoutManager = LinearLayoutManager(activity)
        rvJogosNaoTerminados.adapter = adapter
        rvJogosNaoTerminados.emptyView = view.layoutEmpty

        viewModel.getUnfinishedGames().subscribe { games ->
            adapter.jogos = games
        }

        adapter.setOnMenuItemClickListener(object : MyGamesAdapter.OnMenuOverflowClickListener {
            override fun onMenuItemClick(menu: MenuItem, game: Game) {
                when (menu.itemId) {
                    R.id.navRemover -> {
                        EventBus.getDefault().post(ExcluirJogoEvent(game.id))
                    }
                    R.id.navGerenciarListas -> {
                        EventBus.getDefault().post(GerenciarListasEvent(game.id))
                    }
                    R.id.navAtualizarProgresso -> {
                        dialogAtualizarProgresso(game)
                    }
                    R.id.navMarcarComoZerado -> {
                        game.progress.beaten = true
                        viewModel.updateGameProgress(game)
                        requireContext().toast(getString(R.string.msg_jogo_movido_zerados))
                    }
                }
            }
        })
    }


    private fun dialogAtualizarProgresso(game: Game) {

        val view = LayoutInflater.from(context).inflate(R.layout.fragment_meu_progresso, null, false)

        view.sbProgresso.setOnProgressChangeListener(object : DiscreteSeekBar.OnProgressChangeListener {
            override fun onProgressChanged(seekBar: DiscreteSeekBar?, value: Int, fromUser: Boolean) {
                view.tvPorcentagemProgresso.text = "$value%"
                view.chkJogoZerado.isChecked = value == 100
            }

            override fun onStartTrackingTouch(seekBar: DiscreteSeekBar?) {}

            override fun onStopTrackingTouch(seekBar: DiscreteSeekBar?) {}

        })

        view.etHorasJogadas.setText("${game.progress.hoursPlayed}")
        view.sbProgresso.progress = game.progress.percentage
        view.chkJogoZerado.isChecked = game.progress.beaten


        val dialog = AlertDialog.Builder(requireContext())
                .setView(view)
                .setPositiveButton(getString(R.string.atualizar)) { _, _ ->
                    var horasJogadasStr = view.etHorasJogadas.text.toString().trim()
                    horasJogadasStr = if (horasJogadasStr.isEmpty()) "0" else horasJogadasStr

                    game.progress.hoursPlayed = Integer.parseInt(horasJogadasStr)
                    game.progress.percentage = view.sbProgresso.progress
                    game.progress.beaten = view.chkJogoZerado.isChecked

                    viewModel.updateGameProgress(game)

                    requireContext().toast(getString(R.string.progresso_atualizado))
                }
                .setNegativeButton(getString(R.string.cancelar), null)
                .create()

        dialog.show()
    }

}