package com.matheusfroes.gamerguide.ui.addgames

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import com.matheusfroes.gamerguide.EndlessScrollListener
import com.matheusfroes.gamerguide.R
import com.matheusfroes.gamerguide.ViewModelFactory
import com.matheusfroes.gamerguide.data.model.FormaCadastro
import com.matheusfroes.gamerguide.data.model.Game
import com.matheusfroes.gamerguide.data.model.GameList
import com.matheusfroes.gamerguide.esconderTeclado
import com.matheusfroes.gamerguide.ui.BaseActivity
import kotlinx.android.synthetic.main.activity_adicionar_jogos.*
import kotlinx.android.synthetic.main.activity_detalhes_jogo.*
import kotlinx.android.synthetic.main.dialog_remover_jogo.view.*
import org.greenrobot.eventbus.EventBus
import org.jetbrains.anko.toast
import javax.inject.Inject


class AddGamesActivity : BaseActivity() {
    val adapter: AdicionarJogosAdapter by lazy {
        AdicionarJogosAdapter(this)
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var viewModel: AddGamesViewModel

    var queryDigitada = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_adicionar_jogos)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(AddGamesViewModel::class.java)

        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rvJogos.layoutManager = layoutManager
        rvJogos.adapter = adapter

        val scrollListener: EndlessScrollListener = object : EndlessScrollListener(layoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                viewModel.searchGames(queryDigitada)
            }
        }
        rvJogos.addOnScrollListener(scrollListener)

        adapter.setOnMenuItemClickListener(object : AdicionarJogosAdapter.OnAdicionarJogoListener {
            override fun onMenuItemClick(action: String, jogo: Game) {
                when (action) {
                    "adicionar_jogo" -> {
                        toast(getString(R.string.jogo_adicionado))
                        val jogoInserido = jogosDAO.obterJogoPorFormaCadastro(jogo.id, formaCadastro = FormaCadastro.CADASTRO_POR_LISTA)
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

        viewModel.games.observe(this, Observer { games ->
            adapter.games = games.orEmpty()
        })

        // Obter games mais populares do momento
        viewModel.searchGames()

        etNomeJogo.setOnEditorActionListener { _, _, _ ->
            esconderTeclado(this)
            scrollListener.resetState()
            queryDigitada = etNomeJogo.text.toString()

            viewModel.searchGames(queryDigitada)
            true
        }
    }

    private fun dialogRemoverJogo(jogoId: Long) {
        val view = LayoutInflater.from(this).inflate(R.layout.dialog_remover_jogo, null, false)

        val dialog = AlertDialog.Builder(this)
                .setView(view)
                .setPositiveButton(getString(R.string.confirmar)) { _, _ ->
                    val removerDasListas = view.chkRemoverDasListas.isChecked

                    val jogo = jogosDAO.obterJogoPorFormaCadastro(jogoId)

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

    private fun dialogGerenciarListas(jogo: Game) {
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

        val jogosAdicionarNaLista = mutableListOf<GameList>()
        val jogosRemoverDaLista = mutableListOf<GameList>()

        val dialog = AlertDialog.Builder(this)
                .setTitle(getString(R.string.gerenciar_listas))
                .setNegativeButton(getString(R.string.cancelar)) { _, _ -> }
                .setMultiChoiceItems(listasStr, jogoJaCadastrado.toBooleanArray()) { _, which, isChecked ->
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
                .setPositiveButton(getString(R.string.confirmar)) { _, _ ->
                    adicionarJogosLista(jogosAdicionarNaLista, jogo)
                    removerJogosLista(jogosRemoverDaLista, jogo)
                }
                .create()

        dialog.show()

    }

    private fun removerJogosLista(jogosRemoverDaLista: MutableList<GameList>, jogo: Game) {
        jogosRemoverDaLista.forEach { lista ->
            listasDAO.removerJogoDaLista(jogo.id, lista.id)
        }

        if (jogosRemoverDaLista.size == 1) {
            toast(getString(R.string.msg_jogo_removido_lista))
        } else if (jogosRemoverDaLista.size > 1) {
            toast(getString(R.string.msg_jogo_removido_listas))
        }
    }

    private fun adicionarJogosLista(jogosAdicionarNaLista: MutableList<GameList>, jogo: Game) {

        // Verificando se o jogo já está cadastrado no banco na hora de inserir na lista
        if (jogosDAO.obterJogoPorFormaCadastro(jogo.id) == null) {
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
