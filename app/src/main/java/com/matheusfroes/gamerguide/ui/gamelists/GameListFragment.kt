package com.matheusfroes.gamerguide.ui.gamelists

import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.TextInputEditText
import android.support.v7.app.AlertDialog
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.matheusfroes.gamerguide.*
import com.matheusfroes.gamerguide.data.model.GameList
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.activity_listas.*
import kotlinx.android.synthetic.main.dialog_adicionar_lista.view.*
import kotlinx.android.synthetic.main.fab.*
import org.jetbrains.anko.toast
import javax.inject.Inject


class GameListFragment : DaggerFragment() {
    val adapter: ListasAdapter by lazy {
        ListasAdapter(context())
    }
    var etNomeLista: TextInputEditText? = null

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var viewModel: GameListViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
            inflater.inflate(R.layout.activity_listas, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(GameListViewModel::class.java)

        fab.setOnClickListener { dialogAdicionarLista() }

        val layoutManager = LinearLayoutManager(context(), LinearLayoutManager.VERTICAL, false)
        rvListas.layoutManager = layoutManager
        rvListas.emptyView = layoutEmpty

        val mDividerItemDecoration = DividerItemDecoration(
                rvListas.context,
                layoutManager.orientation
        )
        rvListas.addItemDecoration(mDividerItemDecoration)
        rvListas.adapter = adapter

        viewModel.getLists().subscribe { lists ->
            adapter.lists = lists
        }


        adapter.setOnListaClickListener(object : ListasAdapter.OnListaClickListener {
            override fun onListaClick(listaId: Long) {
                val intent = Intent(context(), DetalhesListaActivity::class.java)
                intent.putExtra("lista_id", listaId)
                startActivity(intent)
            }
        })
    }

    private fun dialogAdicionarLista() {
        val view = View.inflate(context(), R.layout.dialog_adicionar_lista, null)

        val dialog = AlertDialog.Builder(context())
                .setView(view)
                .setPositiveButton(getString(R.string.adicionar)) { _, _ ->
                    etNomeLista = view.etNomeLista
                    adicionarLista()
                }
                .setNegativeButton(getString(R.string.cancelar)) { _, _ ->

                }
                .create()

        dialog.show()
        val botaoAdicionar = dialog.getButton(AlertDialog.BUTTON_POSITIVE)

        botaoAdicionar.isEnabled = false

        view.etNomeLista.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {}

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(text: CharSequence, p1: Int, p2: Int, p3: Int) {
                val listaExistente = viewModel.listAlreadyAdded(text.trim().toString())

                if (listaExistente) {
                    view.tilNomeLista.isErrorEnabled = true
                    view.tilNomeLista.error = getString(R.string.msg_lista_existente)
                } else {
                    view.tilNomeLista.error = null
                    view.tilNomeLista.isErrorEnabled = false
                }

                botaoAdicionar.isEnabled = text.isNotEmpty() && !listaExistente
            }
        })
    }

    private fun adicionarLista() {
        val nomeLista = etNomeLista?.text.toString()
        viewModel.addList(GameList(name = nomeLista))

        activity().toast(getString(R.string.lista_adicionada))
    }

    override fun onResume() {
        super.onResume()

        (activity() as MainActivity).currentScreen(R.id.menu_listas)
    }
}