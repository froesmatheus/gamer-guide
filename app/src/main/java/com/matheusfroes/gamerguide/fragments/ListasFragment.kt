package com.matheusfroes.gamerguide.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.matheusfroes.gamerguide.R
import com.matheusfroes.gamerguide.adapters.ListasAdapter
import com.matheusfroes.gamerguide.models.Lista
import kotlinx.android.synthetic.main.fab.view.*
import kotlinx.android.synthetic.main.fragment_listas.view.*
import kotlinx.android.synthetic.main.toolbar.*

/**
 * Created by matheus_froes on 19/09/2017.
 */
class ListasFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_listas, container, false)
        activity.tabLayout.visibility = View.GONE

        view.fab.setOnClickListener { dialogAdicionarLista() }

        val adapter = ListasAdapter(context, listOf(Lista("Lista de Desejos", 0), Lista("Melhores jogos 2017", 0), Lista("Piores jogos 2017", 0)))

        view.rvListas.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        view.rvListas.adapter = adapter


        return view
    }

    private fun dialogAdicionarLista() {
        val dialog = AlertDialog.Builder(activity)
                .setView(R.layout.dialog_adicionar_lista)
                .setPositiveButton(getString(R.string.adicionar)) { _, _ ->

                }
                .setNegativeButton(getString(R.string.cancelar)) { _, _ ->

                }
                .create()

        dialog.show()
    }
}