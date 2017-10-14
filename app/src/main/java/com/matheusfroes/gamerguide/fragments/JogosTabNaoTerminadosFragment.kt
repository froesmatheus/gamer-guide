package com.matheusfroes.gamerguide.fragments

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
import com.matheusfroes.gamerguide.JogoAdicionadoRemovidoEvent
import com.matheusfroes.gamerguide.R
import com.matheusfroes.gamerguide.activities.TelaPrincipalViewModel
import com.matheusfroes.gamerguide.adapters.MeusJogosAdapter
import com.matheusfroes.gamerguide.db.JogosDAO
import com.matheusfroes.gamerguide.db.ListasDAO
import com.matheusfroes.gamerguide.models.Lista
import kotlinx.android.synthetic.main.fragment_jogos_nao_terminados.view.*
import kotlinx.android.synthetic.main.fragment_meu_progresso.view.*
import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.jetbrains.anko.toast

/**
 * Created by matheusfroes on 20/09/2017.
 */
class JogosTabNaoTerminadosFragment : Fragment() {
    val adapter: MeusJogosAdapter by lazy {
        MeusJogosAdapter(context)
    }
    private val viewModel: TelaPrincipalViewModel by lazy {
        ViewModelProviders.of(this).get(TelaPrincipalViewModel::class.java)
    }
    val listasDAO: ListasDAO by lazy {
        ListasDAO(context)
    }
    private val jogosDAO: JogosDAO by lazy { JogosDAO(context) }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_jogos_nao_terminados, container, false)

        view.rvJogosNaoTerminados.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        view.rvJogosNaoTerminados.emptyView = view.layoutEmpty
        view.rvJogosNaoTerminados.adapter = adapter


//        val jogos = jogosDAO.obterJogosPorStatus(zerados = false)
//        adapter.preencherLista(jogos)

        viewModel.jogos.observe(this, Observer { jogos ->
            adapter.preencherLista(jogos?.filter { !it.progresso.zerado } ?: listOf())
        })

        viewModel.atualizarListaJogos()


        adapter.setOnMenuItemClickListener(object : MeusJogosAdapter.OnMenuOverflowClickListener {
            override fun onMenuItemClick(menu: MenuItem, jogoId: Long) {
                when (menu.itemId) {
                    R.id.navRemover -> {
                        dialogRemoverJogo(jogoId)
                    }
                    R.id.navGerenciarListas -> {
                        dialogGerenciarListas(jogoId)
                    }
                    R.id.navAtualizarProgresso -> {
                        dialogAtualizarProgresso(jogoId)
                    }
                    R.id.navMarcarComoZerado -> {
                        viewModel.marcarComoZerado(jogoId)
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

        view.etHorasJogadas.setText("${progressoJogo.horasJogadas}")
        view.sbProgresso.progress = progressoJogo.progressoPerc
        view.chkJogoZerado.isChecked = progressoJogo.zerado


        val dialog = AlertDialog.Builder(context)
                .setView(view)
                .setPositiveButton(getString(R.string.atualizar)) { dialogInterface, i ->
                    var horasJogadasStr = view.etHorasJogadas.text.toString().trim()
                    horasJogadasStr = if (horasJogadasStr.isEmpty()) "0" else horasJogadasStr

                    progressoJogo.horasJogadas = Integer.parseInt(horasJogadasStr)
                    progressoJogo.progressoPerc = view.sbProgresso.progress
                    progressoJogo.zerado = view.chkJogoZerado.isChecked

                    viewModel.atualizarProgressoJogo(jogoId, progressoJogo)

                    context.toast(getString(R.string.progresso_atualizado))
                }
                .setNegativeButton(getString(R.string.cancelar), null)
                .create()

        dialog.show()
    }

    private fun dialogRemoverJogo(jogoId: Long) {
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_remover_jogo, null, false)

        val dialog = AlertDialog.Builder(context)
                .setView(view)
                .setPositiveButton(getString(R.string.confirmar)) { dialogInterface, i ->
                    viewModel.removerJogo(jogoId)
                    context.toast(context.getString(R.string.jogo_removido))
                }
                .setNegativeButton(getString(R.string.cancelar), null)
                .create()

        dialog.show()

    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    fun onMessageEvent(event: JogoAdicionadoRemovidoEvent) {
        viewModel.atualizarListaJogos()

        EventBus.getDefault().removeStickyEvent(event)
    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }

    private fun dialogGerenciarListas(jogoId: Long) {
        val listas = listasDAO.obterListas()
        val jogoJaCadastrado = mutableListOf<Boolean>()

        listas.forEach { lista ->
            if (listasDAO.listaContemJogo(jogoId, lista.id)) {
                jogoJaCadastrado.add(true)
            } else {
                jogoJaCadastrado.add(false)
            }
        }

        val listasStr = listas.map { it.toString() }.toTypedArray()

        val jogosAdicionarNaLista = mutableListOf<Lista>()
        val jogosRemoverDaLista = mutableListOf<Lista>()

        val dialog = AlertDialog.Builder(context)
                .setTitle(getString(R.string.gerenciar_listas))
                .setNegativeButton(getString(R.string.cancelar)) { dialogInterface, i -> }
                .setMultiChoiceItems(listasStr, jogoJaCadastrado.toBooleanArray()) { dialog, which, isChecked ->
                    if (isChecked && !jogoJaCadastrado[which]) {
                        jogosAdicionarNaLista.add(listas[which])
                    } else if (isChecked && jogoJaCadastrado[which]) {
                        jogosRemoverDaLista.remove(listas[which])
                    } else if (jogosAdicionarNaLista.contains(listas[which])) {
                        jogosAdicionarNaLista.remove(listas[which])
                    } else if (!isChecked && jogoJaCadastrado[which] && !jogosRemoverDaLista.contains(listas[which])) {
                        jogosRemoverDaLista.add(listas[which])
                    }
                }
                .setPositiveButton(getString(R.string.confirmar)) { dialogInterface, i ->
                    adicionarJogosLista(jogosAdicionarNaLista, jogoId)
                    removerJogosLista(jogosRemoverDaLista, jogoId)
                }
                .create()

        dialog.show()

    }

    private fun removerJogosLista(jogosRemoverDaLista: MutableList<Lista>, jogoId: Long) {
        jogosRemoverDaLista.forEach { lista ->
            listasDAO.removerJogoDaLista(jogoId, lista.id)
        }

        if (jogosRemoverDaLista.size == 1) {
            context.toast(getString(R.string.msg_jogo_removido_lista))
        } else if (jogosRemoverDaLista.size > 1) {
            context.toast(getString(R.string.msg_jogo_removido_listas))
        }
    }

    private fun adicionarJogosLista(jogosAdicionarNaLista: MutableList<Lista>, jogoId: Long) {
        jogosAdicionarNaLista.forEach { lista ->
            listasDAO.adicionarJogoNaLista(jogoId, lista.id)
        }
        if (jogosAdicionarNaLista.size == 1) {
            context.toast(getString(R.string.msg_jogo_adicionado_lista))
        } else if (jogosAdicionarNaLista.size > 1) {
            context.toast(getString(R.string.msg_jogo_adicionado_listas))
        }
    }
}