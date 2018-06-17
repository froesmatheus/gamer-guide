package com.matheusfroes.gamerguide.ui.mygames

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import com.matheusfroes.gamerguide.*
import com.matheusfroes.gamerguide.ui.TelaPrincipalViewModel
import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.jetbrains.anko.toast


class JogosTabZeradosFragment : Fragment() {
    val adapter: MeusJogosAdapter by lazy {
        MeusJogosAdapter(context())
    }
    private val viewModel: TelaPrincipalViewModel by lazy {
        ViewModelProviders.of(this).get(TelaPrincipalViewModel::class.java)
    }
    val listasDAO: ListasDAO by lazy {
        ListasDAO(context())
    }
    private val jogosDAO: JogosDAO by lazy { JogosDAO(context()) }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_jogos_nao_terminados, container, false)

        view.rvJogosNaoTerminados.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        view.rvJogosNaoTerminados.emptyView = view.layoutEmpty
        view.rvJogosNaoTerminados.adapter = adapter


        //adapter.preencherLista(jogosDAO.obterJogosPorStatus(zerados = true))
        viewModel.jogos.observe(this, Observer { jogos ->
            adapter.preencherLista(jogos?.filter { it.progress.beaten } ?: listOf())
        })

        viewModel.atualizarListaJogos()



        adapter.setOnMenuItemClickListener(object : MeusJogosAdapter.OnMenuOverflowClickListener {
            override fun onMenuItemClick(menu: MenuItem, jogoId: Long) {
                when (menu.itemId) {
                    R.id.navRemover -> {
                        EventBus.getDefault().post(ExcluirJogoEvent(jogoId))
                    }
                    R.id.navGerenciarListas -> {
                        EventBus.getDefault().post(GerenciarListasEvent(jogoId))
                    }
                    R.id.navAtualizarProgresso -> {
                        dialogAtualizarProgresso(jogoId)
                    }
                    R.id.navMarcarComoNaoTerminado -> {
                        viewModel.alterarStatusJogo(jogoId, zerado = false)
                        context().toast(getString(R.string.msg_jogo_movido_nao_terminados))
                    }
                }
            }
        })
        return view
    }

    private fun dialogAtualizarProgresso(jogoId: Long) {
        val progressoJogo = viewModel.obterProgressoJogo(jogoId)!!

        val view = LayoutInflater.from(context).inflate(R.layout.fragment_meu_progresso, null, false)

        view.sbProgresso.setOnProgressChangeListener(object : DiscreteSeekBar.OnProgressChangeListener {
            override fun onProgressChanged(seekBar: DiscreteSeekBar?, value: Int, fromUser: Boolean) {
                view.tvPorcentagemProgresso.text = "$value%"
                view.chkJogoZerado.isChecked = value == 100
            }

            override fun onStartTrackingTouch(seekBar: DiscreteSeekBar?) {}

            override fun onStopTrackingTouch(seekBar: DiscreteSeekBar?) {}

        })

        view.etHorasJogadas.setText("${progressoJogo.hoursPlayed}")
        view.sbProgresso.progress = progressoJogo.percentage
        view.chkJogoZerado.isChecked = progressoJogo.beaten


        val dialog = AlertDialog.Builder(context())
                .setView(view)
                .setPositiveButton(getString(R.string.atualizar)) { _, _ ->
                    var horasJogadasStr = view.etHorasJogadas.text.toString().trim()
                    horasJogadasStr = if (horasJogadasStr.isEmpty()) "0" else horasJogadasStr

                    progressoJogo.hoursPlayed = Integer.parseInt(horasJogadasStr)
                    progressoJogo.percentage = view.sbProgresso.progress
                    progressoJogo.beaten = view.chkJogoZerado.isChecked

                    viewModel.atualizarProgressoJogo(jogoId, progressoJogo)

                    context().toast(getString(R.string.progresso_atualizado))
                }
                .setNegativeButton(getString(R.string.cancelar), null)
                .create()

        dialog.show()
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    fun jogoAdicionadoRemovido(event: JogoAdicionadoRemovidoEvent) {
        viewModel.atualizarListaJogos()

        EventBus.getDefault().removeStickyEvent(event)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun atualizarListaJogos(event: AtualizarListaJogosEvent) {
        viewModel.atualizarListaJogos()
    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }
}