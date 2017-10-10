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
import com.matheusfroes.gamerguide.db.ListasDAO
import com.matheusfroes.gamerguide.models.Lista
import kotlinx.android.synthetic.main.fragment_jogos_nao_terminados.view.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.jetbrains.anko.toast

/**
 * Created by matheusfroes on 20/09/2017.
 */
class JogosTabFragment : Fragment() {
    val adapter: MeusJogosAdapter by lazy {
        MeusJogosAdapter(context)
    }
    private val viewModel: TelaPrincipalViewModel by lazy {
        ViewModelProviders.of(this).get(TelaPrincipalViewModel::class.java)
    }
    val tipoJogo: String by lazy {
        arguments.getString("tipo_jogo")
    }
    val listasDAO: ListasDAO by lazy {
        ListasDAO(context)
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_jogos_nao_terminados, container, false)

        view.rvJogosNaoTerminados.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        view.rvJogosNaoTerminados.emptyView = view.layoutEmpty
        view.rvJogosNaoTerminados.adapter = adapter


        viewModel.jogosNaoTerminados.observe(this, Observer { jogos ->
            adapter.preencherLista(jogos ?: listOf())
        })



        if (tipoJogo == "nao_terminado") {
            viewModel.getJogosNaoTerminados()
        } else {
            viewModel.getJogosZerados()
        }

//        view.rvJogosNaoTerminados.addOnScrollListener(object : RecyclerView.OnScrollListener() {
//
//            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
//                if (dy > 0 && activity.bottomNavigation.isShown) {
//                    activity.bottomNavigation.visibility = View.GONE
//                } else if (dy < 0) {
//                    activity.bottomNavigation.visibility = View.VISIBLE
//                }
//            }
//        })

        adapter.setOnMenuItemClickListener(object : MeusJogosAdapter.OnMenuOverflowClickListener {
            override fun onMenuItemClick(menu: MenuItem, itemId: Long) {
                when (menu.itemId) {
                    R.id.navRemover -> {
                        viewModel.removerJogo(itemId)
                        context.toast(context.getString(R.string.jogo_removido))
                    }
                    R.id.navGerenciarListas -> {
                        dialogGerenciarListas(itemId)
                    }
                    R.id.navAtualizarProgresso -> {

                    }
                    R.id.navMarcarComoZerado -> {
                        viewModel.marcarComoZerado(itemId)
                    }
                }
            }
        })
        return view
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    fun onMessageEvent(event: JogoAdicionadoRemovidoEvent) {
        if (tipoJogo == "nao_terminado") {
            viewModel.getJogosNaoTerminados()
        } else {
            viewModel.getJogosZerados()
        }

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
        val listaDAO = ListasDAO(context)
        val listas = listaDAO.obterListas()
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
    }

    private fun adicionarJogosLista(jogosAdicionarNaLista: MutableList<Lista>, jogoId: Long) {
        jogosAdicionarNaLista.forEach { lista ->
            listasDAO.adicionarJogoNaLista(jogoId, lista.id)
        }
    }
}