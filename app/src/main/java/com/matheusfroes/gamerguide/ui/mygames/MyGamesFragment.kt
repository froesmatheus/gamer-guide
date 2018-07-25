package com.matheusfroes.gamerguide.ui.meusjogos

import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.matheusfroes.gamerguide.AtualizarListaJogosEvent
import com.matheusfroes.gamerguide.ExcluirJogoEvent
import com.matheusfroes.gamerguide.GerenciarListasEvent
import com.matheusfroes.gamerguide.R
import com.matheusfroes.gamerguide.data.db.JogosDAO
import com.matheusfroes.gamerguide.data.db.ListasDAO
import com.matheusfroes.gamerguide.data.models.FormaCadastro
import com.matheusfroes.gamerguide.data.models.Lista
import com.matheusfroes.gamerguide.ui.TelaPrincipalViewModel
import com.matheusfroes.gamerguide.ui.adicionarjogos.AdicionarJogosActivity
import kotlinx.android.synthetic.main.activity_meus_jogos.*
import kotlinx.android.synthetic.main.dialog_remover_jogo.view.*
import kotlinx.android.synthetic.main.toolbar.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.jetbrains.anko.toast

class MeusJogosActivity : Fragment() {
    private val viewModel: TelaPrincipalViewModel by lazy {
        ViewModelProviders.of(this).get(TelaPrincipalViewModel::class.java)
    }
    private val listasDAO: ListasDAO by lazy {
        ListasDAO(activity)
    }
    private val jogosDAO: JogosDAO by lazy { JogosDAO(activity) }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
            inflater.inflate(R.layout.activity_meus_jogos, container, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.tabLayout?.visibility = View.VISIBLE

        val adapter = JogosFragmentAdapter(activity, activity.supportFragmentManager)
        viewPager.adapter = adapter

        activity?.tabLayout?.visibility = View.VISIBLE
        activity?.tabLayout?.setupWithViewPager(viewPager)
        fab.setOnClickListener {
            startActivity(Intent(activity, AdicionarJogosActivity::class.java))
        }

        activity.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {}

            override fun onTabUnselected(tab: TabLayout.Tab?) {}

            override fun onTabSelected(tab: TabLayout.Tab?) {
                activity?.appBar?.setExpanded(true, true)
            }

        })
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun removerJogoEvent(event: ExcluirJogoEvent) {
        dialogRemoverJogo(event.jogoId)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun gerenciarListasEvent(event: GerenciarListasEvent) {
        dialogGerenciarListas(event.jogoId)
    }

    override fun onStart() {
        super.onStart()

        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }

    private fun dialogRemoverJogo(jogoId: Long) {
        val view = LayoutInflater.from(activity).inflate(R.layout.dialog_remover_jogo, null, false)

        val dialog = AlertDialog.Builder(activity)
                .setView(view)
                .setPositiveButton(getString(R.string.confirmar)) { _, _ ->
                    val removerDasListas = view.chkRemoverDasListas.isChecked

                    val jogo = jogosDAO.obterJogoPorFormaCadastro(jogoId)

                    if (removerDasListas) {
                        listasDAO.removerJogoTodasListas(jogoId)
                        jogosDAO.remover(jogoId)
                    } else {
                        jogo?.formaCadastro = FormaCadastro.CADASTRO_POR_LISTA
                        jogosDAO.atualizar(jogo!!)
                    }

                    EventBus.getDefault().post(AtualizarListaJogosEvent())
                    activity.toast(getString(R.string.jogo_removido))
                }
                .setNegativeButton(getString(R.string.cancelar), null)
                .create()

        dialog.show()
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

        val dialog = AlertDialog.Builder(activity)
                .setTitle(getString(R.string.gerenciar_listas))
                .setNegativeButton(getString(R.string.cancelar)) { _, _ -> }
                .setMultiChoiceItems(listasStr, jogoJaCadastrado.toBooleanArray()) { _, which, isChecked ->
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
                .setPositiveButton(getString(R.string.confirmar)) { _, _ ->
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
            activity.toast(getString(R.string.msg_jogo_removido_lista))
        } else if (jogosRemoverDaLista.size > 1) {
            activity.toast(getString(R.string.msg_jogo_removido_listas))
        }

        EventBus.getDefault().post(AtualizarListaJogosEvent())
    }

    private fun adicionarJogosLista(jogosAdicionarNaLista: MutableList<Lista>, jogoId: Long) {
        jogosAdicionarNaLista.forEach { lista ->
            listasDAO.adicionarJogoNaLista(jogoId, lista.id)
        }
        if (jogosAdicionarNaLista.size == 1) {
            activity.toast(getString(R.string.msg_jogo_adicionado_lista))
        } else if (jogosAdicionarNaLista.size > 1) {
            activity.toast(getString(R.string.msg_jogo_adicionado_listas))
        }
    }
}