package com.matheusfroes.gamerguide.activities

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.matheusfroes.gamerguide.EndlessRecyclerViewScrollListener
import com.matheusfroes.gamerguide.JogoAdicionadoRemovidoEvent
import com.matheusfroes.gamerguide.R
import com.matheusfroes.gamerguide.adapters.AdicionarJogosAdapter
import com.matheusfroes.gamerguide.db.JogosDAO
import com.matheusfroes.gamerguide.db.ListasDAO
import com.matheusfroes.gamerguide.esconderTeclado
import com.matheusfroes.gamerguide.models.Jogo
import com.matheusfroes.gamerguide.models.Lista
import kotlinx.android.synthetic.main.activity_adicionar_jogos.*
import org.greenrobot.eventbus.EventBus
import org.jetbrains.anko.toast


class AdicionarJogosActivity : AppCompatActivity() {
    val adapter: AdicionarJogosAdapter by lazy {
        AdicionarJogosAdapter(this)
    }
    private val viewModel: AdicionarJogosViewModel by lazy {
        ViewModelProviders.of(this).get(AdicionarJogosViewModel::class.java)
    }
    private val jogosDAO: JogosDAO by lazy {
        JogosDAO(this)
    }
    private val listasDAO: ListasDAO by lazy {
        ListasDAO(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_adicionar_jogos)

        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rvJogos.layoutManager = layoutManager
        rvJogos.adapter = adapter

        val scrollListener: EndlessRecyclerViewScrollListener = object : EndlessRecyclerViewScrollListener(layoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                viewModel.obterLancamentos(viewModel.nextPageId.value!!)
            }
        }
        rvJogos.addOnScrollListener(scrollListener)

        adapter.setOnMenuItemClickListener(object : AdicionarJogosAdapter.OnAdicionarJogoListener {
            override fun onMenuItemClick(action: String, jogo: Jogo) {
                if (action == "adicionar_jogo") {
                    toast(getString(R.string.jogo_adicionado))
                    jogosDAO.inserir(jogo)
                    EventBus.getDefault().postSticky(JogoAdicionadoRemovidoEvent())
                } else {
                    dialogGerenciarListas(jogo.id)
                }
            }
        })


        viewModel.listaPesquisas.observe(this, Observer { listaJogos ->
            adapter.preencherLista(listaJogos!!)
        })

        // Obter jogos mais populares do momento
        viewModel.obterLancamentos()

        etNomeJogo.setOnEditorActionListener { _, _, _ ->
            esconderTeclado(this)
            viewModel.pesquisarJogos(query = etNomeJogo.text.toString())
            true
        }
    }

    private fun dialogGerenciarListas(jogoId: Long) {
        val listas = listasDAO.obterListas()
        val jogoJaCadastrado = mutableListOf<Boolean>()

        listas.forEach { lista ->
            if (listasDAO.listaContemJogo(jogoId, lista.id)) {
                jogoJaCadastrado.add(true)
            } else {
                jogoJaCadastrado.add(false)
            }
        }

        val listasStr = listas.map { it.toString() }.toTypedArray()

        val jogosAdicionarNaLista = mutableListOf<Lista>()
        val jogosRemoverDaLista = mutableListOf<Lista>()

        val dialog = AlertDialog.Builder(this)
                .setTitle(getString(R.string.gerenciar_listas))
                .setNegativeButton(getString(R.string.cancelar)) { dialogInterface, i -> }
                .setMultiChoiceItems(listasStr, jogoJaCadastrado.toBooleanArray()) { dialog, which, isChecked ->
                    if (isChecked && !jogoJaCadastrado[which]) {
                        jogosAdicionarNaLista.add(listas[which])
                    } else if (isChecked && jogoJaCadastrado[which]) {
                        jogosRemoverDaLista.remove(listas[which])
                    } else if (jogosAdicionarNaLista.contains(listas[which])) {
                        jogosAdicionarNaLista.remove(listas[which])
                    } else if (!isChecked && jogoJaCadastrado[which] && !jogosRemoverDaLista.contains(listas[which])) {
                        jogosRemoverDaLista.add(listas[which])
                    }
                }
                .setPositiveButton(getString(R.string.confirmar)) { dialogInterface, i ->
                    adicionarJogosLista(jogosAdicionarNaLista, jogoId)
                    removerJogosLista(jogosRemoverDaLista, jogoId)
                }
                .create()

        dialog.show()

    }

    private fun removerJogosLista(jogosRemoverDaLista: MutableList<Lista>, jogoId: Long) {
        jogosRemoverDaLista.forEach { lista ->
            listasDAO.removerJogoDaLista(jogoId, lista.id)
        }

        if (jogosRemoverDaLista.size == 1) {
            toast(getString(R.string.msg_jogo_removido_lista))
        } else if (jogosRemoverDaLista.size > 1) {
            toast(getString(R.string.msg_jogo_removido_listas))
        }
    }

    private fun adicionarJogosLista(jogosAdicionarNaLista: MutableList<Lista>, jogoId: Long) {
        jogosAdicionarNaLista.forEach { lista ->
            listasDAO.adicionarJogoNaLista(jogoId, lista.id)
        }
        if (jogosAdicionarNaLista.size == 1) {
            toast(getString(R.string.msg_jogo_adicionado_lista))
        } else if (jogosAdicionarNaLista.size > 1) {
            toast(getString(R.string.msg_jogo_adicionado_listas))
        }
    }
}
