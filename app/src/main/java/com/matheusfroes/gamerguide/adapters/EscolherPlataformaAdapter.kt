package com.matheusfroes.gamerguide.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.matheusfroes.gamerguide.R
import kotlinx.android.synthetic.main.view_escolher_plataforma.view.*

/**
 * Created by matheusfroes on 22/09/2017.
 */
class EscolherPlataformaAdapter(val context: Context, private val plataformas: List<String>) : BaseAdapter() {
    override fun getItemId(position: Int) = 0L

    override fun getView(position: Int, view: View?, parent: ViewGroup?): View {
        val view = LayoutInflater.from(context).inflate(R.layout.view_escolher_plataforma, null, false)

        view.tvNomePlataforma.text = plataformas[position]

        view.setOnClickListener {
            view.cbEscolherPlataforma.isChecked = !view.cbEscolherPlataforma.isChecked
        }

        return view
    }

    override fun getItem(position: Int) = plataformas[position]

    override fun getCount() = plataformas.size
}