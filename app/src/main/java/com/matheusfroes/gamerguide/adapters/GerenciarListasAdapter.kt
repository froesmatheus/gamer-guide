package com.matheusfroes.gamerguide.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.matheusfroes.gamerguide.R
import com.matheusfroes.gamerguide.models.Lista
import kotlinx.android.synthetic.main.view_adicionar_lista_dialog.view.*

/**
 * Created by matheusfroes on 28/09/2017.
 */
class GerenciarListasAdapter(val context: Context, private val listas: List<Lista>) : BaseAdapter() {
    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        val view = LayoutInflater.from(context).inflate(R.layout.view_adicionar_lista_dialog, p2, false)

        view.setOnClickListener { view.chkLista.isChecked = !view.chkLista.isChecked }

        return view
    }

    override fun getItem(position: Int) = listas[position]

    override fun getItemId(position: Int) = 0L

    override fun getCount() = listas.size
}