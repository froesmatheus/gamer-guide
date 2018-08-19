package com.matheusfroes.gamerguide.ui.gamelists

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.TextInputEditText
import android.support.v4.app.Fragment
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.matheusfroes.gamerguide.*
import com.matheusfroes.gamerguide.ui.AddGameListDialog
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_listas.*
import kotlinx.android.synthetic.main.fab.*
import kotlinx.android.synthetic.main.toolbar.*
import kotlinx.android.synthetic.main.toolbar.view.*
import org.jetbrains.anko.toast
import javax.inject.Inject


class GameListsFragment : Fragment() {
    val adapter: GameListAdapter by lazy { GameListAdapter() }
    var etNomeLista: TextInputEditText? = null

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: GameListsViewModel
    private val subscriptions = CompositeDisposable()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
            inflater.inflate(R.layout.activity_listas, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        appInjector.inject(this)
        setupToolbar()

        viewModel = viewModelProvider(viewModelFactory)

        viewModel.lists.observe(this, Observer { gameLists ->
            adapter.gameLists = gameLists.orEmpty()
        })

        fab.setOnClickListener { dialogAdicionarLista() }

        val layoutManager = LinearLayoutManager(activity)
        rvListas.layoutManager = layoutManager

        val mDividerItemDecoration = DividerItemDecoration(
                rvListas.context,
                layoutManager.orientation
        )
        rvListas.addItemDecoration(mDividerItemDecoration)
        rvListas.adapter = adapter

        adapter.gameListClick { gameListId ->
            val intent = Intent(activity, GameListDetailsActivity::class.java)
            intent.putExtra("lista_id", gameListId)
            startActivity(intent)
        }

    }

    private fun dialogAdicionarLista() {
        val addGameDialog = AddGameListDialog.newInstance(AddGameListDialog.ADD_LIST)
        addGameDialog.show(childFragmentManager, "add_game")

        addGameDialog.addButtonClick { gameListName ->
            viewModel.addList(gameListName)
            requireActivity().toast(getString(R.string.lista_adicionada))
        }

        addGameDialog.listAlreadyAdded { gameListName ->
            viewModel.listAlreadyAdded(gameListName)
        }
    }

    private fun setupToolbar() {
        appCompatActivity.setSupportActionBar(toolbar)
        appCompatActivity.supportActionBar?.setDisplayShowTitleEnabled(false)
        toolbar.toolbarTitle.text = "Listas"
    }

    override fun onDestroy() {
        super.onDestroy()

        subscriptions.dispose()
    }
}