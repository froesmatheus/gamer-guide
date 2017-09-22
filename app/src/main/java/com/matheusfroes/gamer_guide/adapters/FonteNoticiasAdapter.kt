package com.matheusfroes.gamer_guide.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.matheusfroes.gamer_guide.R
import com.matheusfroes.gamer_guide.models.FonteNoticia
import kotlinx.android.synthetic.main.view_fonte_noticia.view.*

/**
 * Created by matheusfroes on 21/09/2017.
 */
class FonteNoticiasAdapter(private val context: Context, private val fontes: List<FonteNoticia>) : BaseAdapter() {
    override fun getView(position: Int, view: View?, parent: ViewGroup?): View {
        val view = LayoutInflater.from(context).inflate(R.layout.view_fonte_noticia, null, false)

        val fonteNoticia = fontes[position]

        view.tvNome.text = fonteNoticia.nome
        view.tvWebsite.text = fonteNoticia.website

        return view
    }

    override fun getItem(position: Int) = fontes[position]

    override fun getItemId(position: Int) = 0L

    override fun getCount() = fontes.size
}