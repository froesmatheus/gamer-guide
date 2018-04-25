package com.matheusfroes.gamerguide.ui.adicionarjogos

import android.content.Context
import android.content.Intent
import android.support.v7.widget.PopupMenu
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.matheusfroes.gamerguide.R
import com.matheusfroes.gamerguide.data.db.JogosDAO
import com.matheusfroes.gamerguide.data.models.Jogo
import com.matheusfroes.gamerguide.extra.DialogDetalhesJogo
import com.matheusfroes.gamerguide.formatarData
import com.matheusfroes.gamerguide.ui.detalhesjogo.DetalhesJogoActivity
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.view_jogo_pesquisa.view.*


class AdicionarJogosAdapter(private val context: Context) : RecyclerView.Adapter<AdicionarJogosAdapter.ViewHolder>() {
    private var jogos: MutableList<Jogo> = mutableListOf()
    private var listener: OnAdicionarJogoListener? = null

    fun preencherLista(jogos: List<Jogo>) {
        this.jogos.addAll(jogos)
        notifyDataSetChanged()
    }

    fun limparLista() {
        jogos.clear()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val view = View.inflate(context, R.layout.view_jogo_pesquisa, null)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val jogo = jogos[position]


        holder.itemView.tvNomeJogo.text = jogo.nome
        holder.itemView.textview.text = jogo.dataLancamento.formatarData("dd/MM/yyyy")
        holder.itemView.tvPlataformas.text = jogo.plataformas.joinToString()
        holder.itemView.ivCapaJogo.contentDescription = context.getString(R.string.content_description_capa_jogo, jogo.nome)

        holder.capaVisivel = !jogo.imageCapa.isEmpty()


        if (holder.capaVisivel) {
            holder.itemView.ivCapaJogo.visibility = View.VISIBLE
            Picasso
                    .with(context)
                    .load(jogo.imageCapa.replace("t_thumb", "t_cover_big"))
                    .fit()
                    .into(holder.itemView.ivCapaJogo)
        } else {
            holder.itemView.ivCapaJogo.visibility = View.GONE
        }
    }

    private fun showPopup(v: View, jogo: Jogo) {
        val popup = PopupMenu(context, v)
        val inflater = popup.menuInflater
        inflater.inflate(R.menu.menu_adicionar_jogos, popup.menu)
        popup.show()

        popup.setOnMenuItemClickListener {
            listener?.onMenuItemClick("adicionar_a_lista", jogo)
            true
        }
    }

    fun setOnMenuItemClickListener(listener: OnAdicionarJogoListener) {
        this.listener = listener
    }

    private fun dialogDetalhesJogo(jogo: Jogo) {
        val dao = JogosDAO(context)

        val jogoSalvo = dao.obterJogoPorFormaCadastro(jogo.id) != null


        val textoBotao = if (jogoSalvo) context.getString(R.string.btn_remover) else context.getString(R.string.btn_adicionar)

        val dialog = DialogDetalhesJogo(context, jogo)
                .setPositiveButton(textoBotao) { _, _ ->
                    if (jogoSalvo) {
                        this.listener?.onMenuItemClick("remover_jogo", jogo)
                    } else {
                        this.listener?.onMenuItemClick("adicionar_jogo", jogo)
                    }
                }
                .setNegativeButton(context.getString(R.string.Detalhes)) { _, _ ->
                    val intent = Intent(context, DetalhesJogoActivity::class.java)
                    intent.putExtra("tela_origem", "tela_adicionar")
                    intent.putExtra("jogo", jogo)
                    context.startActivity(intent)
                }
                .create()

        dialog.show()
    }

    override fun getItemCount() = jogos.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var capaVisivel = true

        init {
            itemView.ivOverflow.setOnClickListener {
                val jogo = jogos[adapterPosition]
                showPopup(itemView.ivOverflow, jogo)
            }

            itemView.setOnClickListener {
                val jogo = jogos[adapterPosition]
                dialogDetalhesJogo(jogo)
            }

            itemView.ivCapaJogo.setOnClickListener {
                val jogo = jogos[adapterPosition]
                val intent = Intent(context, CapaJogoTelaCheiaActivity::class.java)
                intent.putExtra("url_imagem", jogo.imageCapa)
                context.startActivity(intent)
            }
        }
    }

    interface OnAdicionarJogoListener {
        fun onMenuItemClick(action: String, jogo: Jogo)
    }
}