package com.matheusfroes.gamerguide.adapters

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.matheusfroes.gamerguide.R
import com.matheusfroes.gamerguide.activities.DetalhesJogoActivity

/**
 * Created by matheusfroes on 21/09/2017.
 */
class JogosNaoTerminadosAdapter(private val context: Context) : RecyclerView.Adapter<JogosNaoTerminadosAdapter.ViewHolder>() {
    private val jogos = listOf("A", "B", "C", "D", "E", "F", "G", "C", "D", "E", "C", "D", "E", "C", "D", "E")

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.setOnClickListener {
            context.startActivity(Intent(context, DetalhesJogoActivity::class.java))
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val view = View.inflate(context, R.layout.view_jogo, null)
        return ViewHolder(view)
    }

    override fun getItemCount() = jogos.size

    inner class ViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView)
}