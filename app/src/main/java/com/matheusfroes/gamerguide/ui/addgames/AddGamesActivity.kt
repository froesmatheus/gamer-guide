package com.matheusfroes.gamerguide.ui.addgames

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import com.matheusfroes.gamerguide.*
import com.matheusfroes.gamerguide.data.model.Game
import com.matheusfroes.gamerguide.data.model.GameList
import com.matheusfroes.gamerguide.data.model.InsertType
import com.matheusfroes.gamerguide.extra.DialogDetalhesJogo
import com.matheusfroes.gamerguide.ui.BaseActivity
import com.matheusfroes.gamerguide.ui.gamedetails.GameDetailsActivity
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import kotlinx.android.synthetic.main.activity_adicionar_jogos.*
import kotlinx.android.synthetic.main.dialog_remover_jogo.view.*
import kotlinx.android.synthetic.main.toolbar.*
import org.greenrobot.eventbus.EventBus
import org.jetbrains.anko.toast
import javax.inject.Inject


class AddGamesActivity : BaseActivity() {
    val adapter: AddGamesAdapter by lazy { AddGamesAdapter() }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: AddGamesViewModel
    private val subscriptions = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_adicionar_jogos)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        appInjector.inject(this)


        viewModel = ViewModelProviders.of(this, viewModelFactory)[AddGamesViewModel::class.java]

        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rvJogos.layoutManager = layoutManager
        rvJogos.adapter = adapter

        val scrollListener: EndlessScrollListener = object : EndlessScrollListener(layoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                searchGames()
            }
        }
        rvJogos.addOnScrollListener(scrollListener)

        adapter.gameDetailsClick = { game ->
            dialogDetalhesJogo(game)
        }

        adapter.onPopupMenuClick = { action: String, game: Game ->
            popupMenuClick(action, game)
        }

        // Obter jogos mais populares do momento
        searchGames()

        etNomeJogo.setOnEditorActionListener { _, _, _ ->
            esconderTeclado(this)
            adapter.limparLista()
            scrollListener.resetState()
            viewModel.nextPageId = ""
            viewModel.queryDigitada = etNomeJogo.text.toString()

            searchGames()
            true
        }
    }

    private fun popupMenuClick(action: String, game: Game) {
        when (action) {
            "adicionar_jogo" -> {
                toast(getString(R.string.jogo_adicionado))
                val jogoInserido = viewModel.getGameByInsertType(game.id, InsertType.INSERT_TO_LIST)
                if (jogoInserido == null) {
                    viewModel.addGame(game)
                } else {
                    jogoInserido.insertType = InsertType.INSERT_BY_SEARCH
                    viewModel.updateGame(jogoInserido)
                }
                EventBus.getDefault().postSticky(JogoAdicionadoRemovidoEvent())
            }
            "remover_jogo" -> {
                dialogRemoverJogo(game.id)
            }
            else -> dialogGerenciarListas(game)
        }
    }

    private fun searchGames() {
        subscriptions += viewModel.searchGames().subscribe { result ->

            when (result) {
                is Result.Complete -> {
                    adapter.preencherLista(result.data)
                }
                is Result.Error -> {

                }
                is Result.InProgress -> {

                }
            }
        }
    }

    private fun dialogRemoverJogo(jogoId: Long) {
        val view = LayoutInflater.from(this).inflate(R.layout.dialog_remover_jogo, null, false)

        val gameIsInGameLists = viewModel.gameIsInGameLists(jogoId)

        if (!gameIsInGameLists) {
            view.chkRemoverDasListas.visibility = View.GONE
        }
        val dialog = AlertDialog.Builder(this)
                .setView(view)
                .setPositiveButton(getString(R.string.confirmar)) { _, _ ->
                    val removerDasListas = view.chkRemoverDasListas.isChecked

                    val jogo = viewModel.getGameByInsertType(jogoId)

                    if (removerDasListas || !gameIsInGameLists) {
                        viewModel.removeGameFromLists(jogoId)
                        viewModel.removeGame(jogoId)
                    } else {
                        jogo?.insertType = InsertType.INSERT_TO_LIST
                        viewModel.updateGame(jogo!!)
                    }

                    EventBus.getDefault().postSticky(JogoAdicionadoRemovidoEvent())
                    toast(getString(R.string.jogo_removido))
                }
                .setNegativeButton(getString(R.string.cancelar), null)
                .create()

        dialog.show()
    }

    private fun dialogGerenciarListas(jogo: Game) {
        val listas = viewModel.getLists()
        val jogoJaCadastrado = mutableListOf<Boolean>()

        listas.forEach { lista ->
            if (viewModel.listContainsGame(jogo.id, lista.id)) {
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
                .setMultiChoiceItems(listasStr, jogoJaCadastrado.toBooleanArray()) { _, which: Int, isChecked: Boolean ->
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
            viewModel.removeGameFromList(jogo.id, lista.id)
        }

        if (jogosRemoverDaLista.size == 1) {
            toast(getString(R.string.msg_jogo_removido_lista))
        } else if (jogosRemoverDaLista.size > 1) {
            toast(getString(R.string.msg_jogo_removido_listas))
        }
    }

    private fun adicionarJogosLista(jogosAdicionarNaLista: MutableList<GameList>, jogo: Game) {

        // Verificando se o jogo já está cadastrado no banco na hora de inserir na lista
        if (viewModel.getGameByInsertType(jogo.id) == null) {
            jogo.insertType = InsertType.INSERT_TO_LIST
            viewModel.addGame(jogo)
        }

        jogosAdicionarNaLista.forEach { lista ->
            viewModel.addGameToList(jogo.id, lista.id)
        }
        if (jogosAdicionarNaLista.size == 1) {
            toast(getString(R.string.msg_jogo_adicionado_lista))
        } else if (jogosAdicionarNaLista.size > 1) {
            toast(getString(R.string.msg_jogo_adicionado_listas))
        }
    }

    private fun dialogDetalhesJogo(game: Game) {
        val jogoSalvo = viewModel.getGameByInsertType(game.id) != null

        val textoBotao = if (jogoSalvo) getString(R.string.btn_remover) else getString(R.string.btn_adicionar)

        val dialog = DialogDetalhesJogo(this, game)
                .setPositiveButton(textoBotao) { _, _ ->
                    if (jogoSalvo) {
                        popupMenuClick("remover_jogo", game)
                    } else {
                        popupMenuClick("adicionar_jogo", game)
                    }
                }
                .setNegativeButton(getString(R.string.Detalhes)) { _, _ ->
                    val intent = Intent(this, GameDetailsActivity::class.java)
                    intent.putExtra("tela_origem", "tela_adicionar")
                    intent.putExtra("jogo", game)
                    startActivity(intent)
                }
                .create()

        dialog.show()
    }

    override fun onDestroy() {
        super.onDestroy()

        subscriptions.dispose()
    }
}
