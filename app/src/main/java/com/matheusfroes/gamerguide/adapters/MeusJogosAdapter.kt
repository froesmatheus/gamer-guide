package com.matheusfroes.gamerguide.adapters

import android.content.Context
import android.content.Intent
import android.support.v7.widget.PopupMenu
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.matheusfroes.gamerguide.R
import com.matheusfroes.gamerguide.activities.DetalhesJogoActivity
import com.matheusfroes.gamerguide.models.Jogo
import kotlinx.android.synthetic.main.view_jogo.view.*


/**
 * Created by matheusfroes on 21/09/2017.
 */
class MeusJogosAdapter(private val context: Context) : RecyclerView.Adapter<MeusJogosAdapter.ViewHolder>() {
    private val jogos = listOf<Jogo>()
    private var listener: android.widget.PopupMenu.OnMenuItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val view = View.inflate(context, R.layout.view_jogo, null)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.setOnClickListener {
            context.startActivity(Intent(context, DetalhesJogoActivity::class.java))
        }

        holder.itemView.ivOverflowMenu.setOnClickListener {
            showPopup(holder.itemView.ivOverflowMenu)
        }
    }

    private fun showPopup(v: View) {
        val popup = PopupMenu(context, v)
        val inflater = popup.menuInflater
        inflater.inflate(R.menu.popup_jogos, popup.menu)
        popup.show()

        popup.setOnMenuItemClickListener {
            listener?.onMenuItemClick(it)!!
        }
    }

    fun setOnMenuItemClickListener(listener: android.widget.PopupMenu.OnMenuItemClickListener) {
        this.listener = listener
    }

    override fun getItemCount() = jogos.size

    inner class ViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView)
}