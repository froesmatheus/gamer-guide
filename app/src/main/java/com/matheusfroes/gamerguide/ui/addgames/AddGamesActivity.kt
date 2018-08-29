package com.matheusfroes.gamerguide.ui.addgames

import android.arch.lifecycle.ViewModelProvider
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.MenuItem
import android.view.View
import com.matheusfroes.gamerguide.R
import com.matheusfroes.gamerguide.data.model.Game
import com.matheusfroes.gamerguide.data.model.GameList
import com.matheusfroes.gamerguide.data.model.InsertType
import com.matheusfroes.gamerguide.extra.*
import com.matheusfroes.gamerguide.ui.BaseActivity
import com.matheusfroes.gamerguide.ui.addgamedialog.AddGameDialog
import com.matheusfroes.gamerguide.ui.gamedetails.GameDetailsActivity
import com.matheusfroes.gamerguide.ui.removegamedialog.RemoveGameDialog
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import kotlinx.android.synthetic.main.activity_adicionar_jogos.*
import kotlinx.android.synthetic.main.toolbar.*
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

        viewModel = viewModelProvider(viewModelFactory)

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
                openAddGameDialog(game)
            }
            "remover_jogo" -> {
                openRemoveGameDialog(game.id)
            }
            else -> dialogGerenciarListas(game)
        }
    }

    private fun openAddGameDialog(game: Game) {
        val addGameDialog = AddGameDialog.newInstance(game)
        addGameDialog.show(supportFragmentManager, "ADD_GAME_DIALOG")

        addGameDialog.gameAddedEvent = { gameAdded ->
            if (gameAdded) {
                snack(R.string.jogo_adicionado)
            }
        }
    }

    private fun searchGames() {
        subscriptions += viewModel.searchGames().subscribe { result ->

            when (result) {
                is Result.Complete -> {
                    adapter.preencherLista(result.data)

                    if (result.data.isEmpty()) {
                        layoutEmpty.visibility = View.VISIBLE
                    }
                }
                is Result.Error -> {}
                is Result.InProgress -> {
                    layoutEmpty.visibility = View.GONE
                }
            }
        }
    }

    private fun openRemoveGameDialog(gameId: Long) {
        val removeGameDialog = RemoveGameDialog.newInstance(gameId)
        removeGameDialog.show(supportFragmentManager, "REMOVE_GAME_DIALOG")

        removeGameDialog.gameRemovedEvent = { gameRemoved ->
            if (gameRemoved) {
                snack(R.string.jogo_removido)
            } else {
                snack(R.string.game_removed_error)
            }
        }
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }

    private fun dialogDetalhesJogo(game: Game) {
        val jogoSalvo = viewModel.isGameAdded(game.id)

        val textoBotao = if (jogoSalvo) getString(R.string.btn_remover) else getString(R.string.btn_adicionar)

        val dialog = GameDetailsDialog(this, game)
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
