package com.matheusfroes.gamerguide.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import com.matheusfroes.gamerguide.R
import com.matheusfroes.gamerguide.adapters.GerenciarListasAdapter
import com.matheusfroes.gamerguide.adapters.MeusJogosAdapter
import com.matheusfroes.gamerguide.db.ListasDAO
import kotlinx.android.synthetic.main.activity_tela_principal.*
import kotlinx.android.synthetic.main.fragment_jogos_nao_terminados.view.*
import kotlinx.android.synthetic.main.layout_gerenciar_listas_dialog.view.*
import org.jetbrains.anko.toast

/**
 * Created by matheusfroes on 20/09/2017.
 */
class JogosNaoTerminadosFragment : Fragment() {
    val adapter: MeusJogosAdapter by lazy {
        MeusJogosAdapter(activity)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_jogos_nao_terminados, container, false)


        view.rvJogosNaoTerminados.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        view.rvJogosNaoTerminados.adapter = adapter

        view.rvJogosNaoTerminados.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                if (dy > 0 && activity.bottomNavigation.isShown) {
                    activity.bottomNavigation.visibility = View.GONE
                } else if (dy < 0) {
                    activity.bottomNavigation.visibility = View.VISIBLE
                }
            }
        })

        adapter.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { menu ->
            when (menu.itemId) {
                R.id.navRemover -> context.toast("Remover")
                R.id.navGerenciarListas -> {
                    dialogGerenciarListas()
                }
            }

            true
        })
        return view
    }

    private fun dialogGerenciarListas() {
        val listaDAO = ListasDAO(context)

        val view = View.inflate(context, R.layout.layout_gerenciar_listas_dialog, null)

        view.rvGerenciarListas.adapter = GerenciarListasAdapter(context, listaDAO.obterListas())

        val dialog = AlertDialog.Builder(context)
                .setTitle("Gerenciar listas")
                .setView(view)
                .setPositiveButton("Confirmar") { dialogInterface, i ->

                }
                .setNegativeButton("Cancelar") { dialogInterface, i ->

                }
                .create()

        dialog.show()
    }
}