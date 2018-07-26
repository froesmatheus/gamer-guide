package com.matheusfroes.gamerguide.ui.gamelists

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.TextInputEditText
import android.support.v4.app.Fragment
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.matheusfroes.gamerguide.AddGameListDialog
import com.matheusfroes.gamerguide.R
import com.matheusfroes.gamerguide.appInjector
import kotlinx.android.synthetic.main.activity_listas.*
import kotlinx.android.synthetic.main.fab.*
import kotlinx.android.synthetic.main.toolbar.*
import org.jetbrains.anko.toast
import javax.inject.Inject


class GameListsFragment : Fragment() {
    val adapter: GameListAdapter by lazy { GameListAdapter() }
    var etNomeLista: TextInputEditText? = null

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: GameListsViewModel


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
            inflater.inflate(R.layout.activity_listas, container, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        appInjector.inject(this)
        activity?.tabLayout?.visibility = View.GONE

        viewModel = ViewModelProviders.of(this, viewModelFactory)[GameListsViewModel::class.java]

        viewModel.gameLists.subscribe { gameLists ->
            adapter.gameLists = gameLists
        }

        fab.setOnClickListener { dialogAdicionarLista() }

        val layoutManager = LinearLayoutManager(activity)
        rvListas.layoutManager = layoutManager
        rvListas.emptyView = layoutEmpty

        val mDividerItemDecoration = DividerItemDecoration(
                rvListas.context,
                layoutManager.orientation
        )
        rvListas.addItemDecoration(mDividerItemDecoration)
        rvListas.adapter = adapter

        adapter.gameListClick { gameListId ->
            val intent = Intent(activity, DetalhesListaActivity::class.java)
            intent.putExtra("lista_id", gameListId)
            startActivity(intent)
        }

    }

    private fun dialogAdicionarLista() {
        val addGameDialog = AddGameListDialog()
        addGameDialog.show(childFragmentManager, "add_game")

        addGameDialog.addButtonClick { gameListName ->
            viewModel.addList(gameListName)
            activity.toast(getString(R.string.lista_adicionada))
        }

        addGameDialog.listAlreadyAdded { gameListName ->
            viewModel.listAlreadyAdded(gameListName)
        }
    }
}