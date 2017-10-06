package com.matheusfroes.gamerguide.fragments

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import com.matheusfroes.gamerguide.JogoAdicionadoRemovidoEvent
import com.matheusfroes.gamerguide.R
import com.matheusfroes.gamerguide.activities.TelaPrincipalViewModel
import com.matheusfroes.gamerguide.adapters.MeusJogosAdapter
import com.matheusfroes.gamerguide.db.ListasDAO
import kotlinx.android.synthetic.main.activity_tela_principal.*
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
        MeusJogosAdapter(activity)
    }
    private val viewModel: TelaPrincipalViewModel by lazy {
        ViewModelProviders.of(this).get(TelaPrincipalViewModel::class.java)
    }
    val tipoJogo: String by lazy {
        arguments.getString("tipo_jogo")
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

        view.rvJogosNaoTerminados.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                if (dy > 0 && activity.bottomNavigation.isShown) {
                    activity.bottomNavigation.visibility = View.GONE
                } else if (dy < 0) {
                    activity.bottomNavigation.visibility = View.VISIBLE
                }
            }
        })

        adapter.setOnMenuItemClickListener(object : MeusJogosAdapter.OnMenuOverflowClickListener {
            override fun onMenuItemClick(menu: MenuItem, itemId: Long) {
                when (menu.itemId) {
                    R.id.navRemover -> {
                        viewModel.removerJogo(itemId)
                        context.toast(context.getString(R.string.jogo_removido))
                    }
                    R.id.navGerenciarListas -> {
                        dialogGerenciarListas()
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

    private fun dialogGerenciarListas() {
        val listaDAO = ListasDAO(context)
        val listas = listaDAO.obterListas()

        val listasStr = listas.map { it.toString() }.toTypedArray()

        val booleans = booleanArrayOf(true, false, true)
        val listasSelecionadas = mutableListOf<Int>()

        val dialog = AlertDialog.Builder(context)
                .setTitle(getString(R.string.gerenciar_listas))
                .setPositiveButton(getString(R.string.confirmar)) { dialogInterface, i ->
                    Log.d("GAMERGUIDE", listasSelecionadas.toString())
                    booleans.forEach {
                        Log.d("GAMERGUIDE", it.toString())
                    }
                }
                .setNegativeButton(getString(R.string.cancelar)) { dialogInterface, i ->

                }
                .setMultiChoiceItems(listasStr, null) { dialog, which, isChecked ->
                    if (isChecked) {
                        listasSelecionadas.add(which)
                    } else if (listasSelecionadas.contains(which)) {
                        listasSelecionadas.remove(which)
                    }
                }
                .create()

        dialog.show()
    }
}