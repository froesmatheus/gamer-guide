package com.matheusfroes.gamer_guide.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.matheusfroes.gamer_guide.R

/**
 * Created by matheusfroes on 21/09/2017.
 */
class JogosNaoTerminadosAdapter(private val context: Context) : RecyclerView.Adapter<JogosNaoTerminadosAdapter.ViewHolder>() {
    private val jogos = listOf("A", "B", "C", "D", "E", "F")

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val view = View.inflate(context, R.layout.view_jogo, null)
        return ViewHolder(view)
    }

    override fun getItemCount() = jogos.size

    inner class ViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView)
}