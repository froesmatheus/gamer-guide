package com.matheusfroes.gamerguide.ui.gamelists

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import com.matheusfroes.gamerguide.AddGameListDialog
import com.matheusfroes.gamerguide.R
import com.matheusfroes.gamerguide.appInjector
import com.matheusfroes.gamerguide.data.model.GameList
import com.matheusfroes.gamerguide.ui.BaseActivity
import com.matheusfroes.gamerguide.ui.adicionarjogos.AdicionarJogosActivity
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
        appInjector.inject(this)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        intent ?: return

        viewModel = ViewModelProviders.of(this, viewModelFactory)[GameListDetailsViewModel::class.java]

        listaId = intent.getLongExtra("lista_id", 0)

        gameList = viewModel.getList(listaId)

        rvJogosLista.layoutManager = LinearLayoutManager(this)
        rvJogosLista.emptyView = layoutEmpty
        rvJogosLista.adapter = adapter

        viewModel.getGamesByList(listaId).subscribe { games ->
            adapter.games = games
        }

        title = gameList.name


        btnAdicionarJogos.setOnClickListener {
            startActivity(Intent(this, AdicionarJogosActivity::class.java))
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
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
//        val view = View.inflate(this, R.layout.dialog_adicionar_lista, null)
//
//        view.etNomeLista.setText(title)
//
//        val dialog = AlertDialog.Builder(this)
//                .setView(view)
//                .setPositiveButton(getString(R.string.atualizar)) { _, _ ->
//                    editarLista()
//                }
//                .setNegativeButton(getString(R.string.cancelar), null)
//                .create()
//
//        dialog.show()
//        val botaoAdicionar = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
//
//        botaoAdicionar.isEnabled = false
//
//        view.etNomeLista.addTextChangedListener(object : TextWatcher {
//            override fun afterTextChanged(p0: Editable?) {}
//
//            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
//
//            override fun onTextChanged(text: CharSequence, p1: Int, p2: Int, p3: Int) {
//                val listaExistente = dao.verificarListaJaExistente(text.trim().toString())
//
//                if (listaExistente && text.toString() != title) {
//                    view.tilNomeLista.isErrorEnabled = true
//                    view.tilNomeLista.error = getString(R.string.msg_lista_existente)
//                } else {
//                    view.tilNomeLista.error = null
//                    view.tilNomeLista.isErrorEnabled = false
//
//                    lista?.nome = text.toString()
//                }
//
//                botaoAdicionar.isEnabled = text.isNotEmpty() && !listaExistente
//            }
//        })
    }
}

