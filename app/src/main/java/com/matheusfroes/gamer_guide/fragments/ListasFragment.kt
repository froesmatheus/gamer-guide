package com.matheusfroes.gamer_guide.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.matheusfroes.gamer_guide.R
import com.matheusfroes.gamer_guide.adapters.ListasAdapter
import com.matheusfroes.gamer_guide.models.Lista
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

        val adapter = ListasAdapter(context, listOf(Lista("Lista de Compras", 0), Lista("Melhores jogos 2017", 0), Lista("Piores jogos 2017", 0)))

        view.rvListas.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        view.rvListas.adapter = adapter


        return view
    }

    private fun dialogAdicionarLista() {
        val dialog = AlertDialog.Builder(activity)
                .setView(R.layout.dialog_adicionar_lista)
                .setPositiveButton("Adicionar") { _, _ ->

                }
                .setNegativeButton("Cancelar") { _, _ ->

                }
                .create()

        dialog.show()
    }
}