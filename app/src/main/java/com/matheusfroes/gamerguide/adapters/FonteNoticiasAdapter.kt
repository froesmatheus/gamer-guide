package com.matheusfroes.gamerguide.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.matheusfroes.gamerguide.R
import com.matheusfroes.gamerguide.models.FonteNoticia
import kotlinx.android.synthetic.main.view_fonte_noticia.view.*

/**
 * Created by matheusfroes on 21/09/2017.
 */
class FonteNoticiasAdapter(
        private val context: Context,
        private val fontes: List<FonteNoticia>) : RecyclerView.Adapter<FonteNoticiasAdapter.ViewHolder>() {

    var listener: AlterarStatusFonteNoticiaListener? = null

    override fun getItemCount() = fontes.size

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        return ViewHolder(View.inflate(context, R.layout.view_fonte_noticia, null))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val fonteNoticia = fontes[position]

        holder.itemView.tvNome.text = fonteNoticia.nome
        holder.itemView.tvWebsite.text = fonteNoticia.website

        holder.itemView.switchFonteNoticia.isChecked = fonteNoticia.ativado

        holder.itemView.switchFonteNoticia.setOnCheckedChangeListener { buttonView, isChecked ->
            listener?.alterarStatus(fonteNoticia.id, isChecked)
        }

        holder.itemView.setOnClickListener {
            holder.itemView.switchFonteNoticia.isChecked = !holder.itemView.switchFonteNoticia.isChecked
        }
    }

    fun setAlterarStatusFonteNoticiaListener(listener: AlterarStatusFonteNoticiaListener) {
        this.listener = listener
    }

    interface AlterarStatusFonteNoticiaListener {
        fun alterarStatus(fonteId: Int, ativo: Boolean)
    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}