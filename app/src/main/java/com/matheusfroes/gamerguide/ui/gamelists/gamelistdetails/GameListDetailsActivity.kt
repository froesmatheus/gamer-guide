package com.matheusfroes.gamerguide.ui.gamelists.gamelistdetails

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import com.matheusfroes.gamerguide.R
import com.matheusfroes.gamerguide.appInjector
import com.matheusfroes.gamerguide.data.model.GameList
import com.matheusfroes.gamerguide.ui.AddGameListDialog
import com.matheusfroes.gamerguide.ui.BaseActivity
import com.matheusfroes.gamerguide.ui.addgames.AddGamesActivity
import kotlinx.android.synthetic.main.activity_detalhes_lista.*
import kotlinx.android.synthetic.main.toolbar.*
import org.jetbrains.anko.toast
import javax.inject.Inject


class GameListDetailsActivity : BaseActivity() {
    private val adapter: GameListGamesAdapter by lazy { GameListGamesAdapter() }
    private lateinit var gameList: GameList
    private var listaId = 0L

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: GameListDetailsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalhes_lista)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        appInjector.inject(this)

        intent ?: return

        viewModel = ViewModelProviders.of(this, viewModelFactory)[GameListDetailsViewModel::class.java]

        listaId = intent.getLongExtra("lista_id", 0)

        gameList = viewModel.getList(listaId)

        rvJogosLista.layoutManager = LinearLayoutManager(this)
        rvJogosLista.adapter = adapter

        viewModel.getGamesByList(listaId).subscribe { games ->
            rvJogosLista.emptyView = layoutEmpty
            adapter.games = games
        }

        title = gameList.name


        btnAdicionarJogos.setOnClickListener {
            startActivity(Intent(this, AddGamesActivity::class.java))
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        if (gameList.isDefault) {
            return super.onCreateOptionsMenu(menu)
        }

        menuInflater.inflate(R.menu.menu_lista, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.navExcluirLista -> {
                viewModel.removeList(listaId)
                toast(getString(R.string.lista_excluida))
                finish()
            }
            R.id.navEditarLista -> {
                dialogEditarLista()
            }
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }

    private fun dialogEditarLista() {
        val addGameDialog = AddGameListDialog.newInstance(AddGameListDialog.UPDATE_LIST)
        addGameDialog.show(supportFragmentManager, "add_game")

        addGameDialog.addButtonClick { gameListName ->
            gameList.name = gameListName
            viewModel.update(gameList)
            recreate()
            toast(getString(R.string.lista_atualizada))
        }

        addGameDialog.listAlreadyAdded { gameListName ->
            viewModel.listAlreadyAdded(gameListName)
        }
    }
}

