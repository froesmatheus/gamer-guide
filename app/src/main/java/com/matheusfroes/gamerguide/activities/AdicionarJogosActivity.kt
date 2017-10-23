package com.matheusfroes.gamerguide.activities

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import com.matheusfroes.gamerguide.*
import com.matheusfroes.gamerguide.adapters.AdicionarJogosAdapter
import com.matheusfroes.gamerguide.db.JogosDAO
import com.matheusfroes.gamerguide.db.ListasDAO
import com.matheusfroes.gamerguide.models.FormaCadastro
import com.matheusfroes.gamerguide.models.Jogo
import com.matheusfroes.gamerguide.models.Lista
import kotlinx.android.synthetic.main.activity_adicionar_jogos.*
import kotlinx.android.synthetic.main.dialog_remover_jogo.view.*
import kotlinx.android.synthetic.main.toolbar.*
import org.greenrobot.eventbus.EventBus
import org.jetbrains.anko.toast


class AdicionarJogosActivity : BaseActivity() {
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
    var queryDigitada = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_adicionar_jogos)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rvJogos.layoutManager = layoutManager
        rvJogos.adapter = adapter

        val scrollListener: EndlessScrollListener = object : EndlessScrollListener(layoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                viewModel.pesquisarJogos(queryDigitada, viewModel.nextPageId.value!!)
            }
        }
        rvJogos.addOnScrollListener(scrollListener)

        adapter.setOnMenuItemClickListener(object : AdicionarJogosAdapter.OnAdicionarJogoListener {
            override fun onMenuItemClick(action: String, jogo: Jogo) {
                when (action) {
                    "adicionar_jogo" -> {
                        toast(getString(R.string.jogo_adicionado))
                        val jogoInserido = jogosDAO.obterJogo(jogo.id, formaCadastro = FormaCadastro.CADASTRO_POR_LISTA)
                        if (jogoInserido == null) {
                            jogosDAO.inserir(jogo)
                        } else {
                            jogoInserido.formaCadastro = FormaCadastro.CADASTRO_POR_BUSCA
                            jogosDAO.atualizar(jogoInserido)
                        }
                        EventBus.getDefault().postSticky(JogoAdicionadoRemovidoEvent())
                    }
                    "remover_jogo" -> {
                        dialogRemoverJogo(jogo.id)
                    }
                    else -> dialogGerenciarListas(jogo)
                }
            }
        })


        viewModel.listaPesquisas.observe(this, Observer { listaJogos ->
            adapter.preencherLista(listaJogos!!)
        })

        // Obter jogos mais populares do momento
        viewModel.pesquisarJogos()

        etNomeJogo.setOnEditorActionListener { _, _, _ ->
            esconderTeclado(this)
            adapter.limparLista()
            scrollListener.resetState()
            queryDigitada = etNomeJogo.text.toString()

            viewModel.pesquisarJogos(query = queryDigitada)
            true
        }
    }

    private fun dialogRemoverJogo(jogoId: Long) {
        val view = LayoutInflater.from(this).inflate(R.layout.dialog_remover_jogo, null, false)

        val dialog = AlertDialog.Builder(this)
                .setView(view)
                .setPositiveButton(getString(R.string.confirmar)) { _, _ ->
                    val removerDasListas = view.chkRemoverDasListas.isChecked

                    val jogo = jogosDAO.obterJogo(jogoId)

                    if (removerDasListas) {
                        listasDAO.removerJogoTodasListas(jogoId)
                        jogosDAO.remover(jogoId)
                    } else {
                        jogo?.formaCadastro = FormaCadastro.CADASTRO_POR_LISTA
                        jogosDAO.atualizar(jogo!!)
                    }

                    EventBus.getDefault().postSticky(JogoAdicionadoRemovidoEvent())
                    toast(getString(R.string.jogo_removido))
                }
                .setNegativeButton(getString(R.string.cancelar), null)
                .create()

        dialog.show()
    }

    private fun dialogGerenciarListas(jogo: Jogo) {
        val listas = listasDAO.obterListas()
        val jogoJaCadastrado = mutableListOf<Boolean>()

        listas.forEach { lista ->
            if (listasDAO.listaContemJogo(jogo.id, lista.id)) {
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
                    adicionarJogosLista(jogosAdicionarNaLista, jogo)
                    removerJogosLista(jogosRemoverDaLista, jogo)
                }
                .create()

        dialog.show()

    }

    private fun removerJogosLista(jogosRemoverDaLista: MutableList<Lista>, jogo: Jogo) {
        jogosRemoverDaLista.forEach { lista ->
            listasDAO.removerJogoDaLista(jogo.id, lista.id)
        }

        if (jogosRemoverDaLista.size == 1) {
            toast(getString(R.string.msg_jogo_removido_lista))
        } else if (jogosRemoverDaLista.size > 1) {
            toast(getString(R.string.msg_jogo_removido_listas))
        }
    }

    private fun adicionarJogosLista(jogosAdicionarNaLista: MutableList<Lista>, jogo: Jogo) {

        // Verificando se o jogo já está cadastrado no banco na hora de inserir na lista
        if (jogosDAO.obterJogo(jogo.id) == null) {
            jogo.formaCadastro = FormaCadastro.CADASTRO_POR_LISTA
            jogosDAO.inserir(jogo)
        }


        jogosAdicionarNaLista.forEach { lista ->
            listasDAO.adicionarJogoNaLista(jogo.id, lista.id)
        }
        if (jogosAdicionarNaLista.size == 1) {
            toast(getString(R.string.msg_jogo_adicionado_lista))
        } else if (jogosAdicionarNaLista.size > 1) {
            toast(getString(R.string.msg_jogo_adicionado_listas))
        }
    }
}
