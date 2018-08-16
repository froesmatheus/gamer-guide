package com.matheusfroes.gamerguide.ui.feed

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.matheusfroes.gamerguide.R
import com.matheusfroes.gamerguide.data.models.FonteNoticia
import kotlinx.android.synthetic.main.view_fonte_noticia.view.*


class FonteNoticiasAdapter(
        private val context: Context,
        private val fontes: List<FonteNoticia>) : RecyclerView.Adapter<FonteNoticiasAdapter.ViewHolder>() {

    var listener: AlterarStatusFonteNoticiaListener? = null

    override fun getItemCount() = fontes.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(View.inflate(context, R.layout.view_fonte_noticia, null))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val fonteNoticia = fontes[position]

        holder.itemView.tvNome.text = fonteNoticia.nome
        holder.itemView.tvWebsite.text = fonteNoticia.website

        holder.itemView.switchFonteNoticia.isChecked = fonteNoticia.ativado
    }

    fun setAlterarStatusFonteNoticiaListener(listener: AlterarStatusFonteNoticiaListener) {
        this.listener = listener
    }

    interface AlterarStatusFonteNoticiaListener {
        fun alterarStatus(fonteId: Int, ativo: Boolean)
    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {
            itemView.setOnClickListener {
                itemView.switchFonteNoticia.isChecked = !itemView.switchFonteNoticia.isChecked
            }

            itemView.switchFonteNoticia.setOnCheckedChangeListener { _, isChecked ->
                val fonteNoticia = fontes[adapterPosition]

                listener?.alterarStatus(fonteNoticia.id, isChecked)
            }
        }
    }
}