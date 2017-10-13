package com.matheusfroes.gamerguide.fragments

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.TextInputEditText
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.matheusfroes.gamerguide.EndlessRecyclerViewScrollListener
import com.matheusfroes.gamerguide.ListaExcluidaEditadaEvent
import com.matheusfroes.gamerguide.R
import com.matheusfroes.gamerguide.activities.DetalhesListaActivity
import com.matheusfroes.gamerguide.adapters.ListasAdapter
import com.matheusfroes.gamerguide.db.ListasDAO
import com.matheusfroes.gamerguide.models.Lista
import kotlinx.android.synthetic.main.dialog_adicionar_lista.view.*
import kotlinx.android.synthetic.main.fab.view.*
import kotlinx.android.synthetic.main.fragment_listas.view.*
import kotlinx.android.synthetic.main.toolbar.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.jetbrains.anko.toast


/**
 * Created by matheus_froes on 19/09/2017.
 */
class ListasFragment : Fragment() {
    val adapter: ListasAdapter by lazy {
        ListasAdapter(context)
    }
    private val dao: ListasDAO by lazy {
        ListasDAO(context)
    }
    var etNomeLista: TextInputEditText? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_listas, container, false)
        activity.tabLayout.visibility = View.GONE

        view.fab.setOnClickListener { dialogAdicionarLista() }

        val layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        view.rvListas.layoutManager = layoutManager
        view.rvListas.emptyView = view.layoutEmpty

        val scrollListener: EndlessRecyclerViewScrollListener = object : EndlessRecyclerViewScrollListener(layoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                context.toast("Page $page Total count: $totalItemsCount")
            }
        }

        val mDividerItemDecoration = DividerItemDecoration(
                view.rvListas.context,
                layoutManager.orientation
        )
        view.rvListas.addItemDecoration(mDividerItemDecoration)
        view.rvListas.addOnScrollListener(scrollListener)

        view.rvListas.adapter = adapter

        adapter.preencherLista(dao.obterListas())

        adapter.setOnListaClickListener(object : ListasAdapter.OnListaClickListener {
            override fun onListaClick(listaId: Int) {
                val intent = Intent(context, DetalhesListaActivity::class.java)
                intent.putExtra("lista_id", listaId)
                startActivity(intent)
            }
        })

        return view
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    fun onMessageEvent(event: ListaExcluidaEditadaEvent) {
        adapter.preencherLista(dao.obterListas())
    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }

    private fun dialogAdicionarLista() {
        val view = View.inflate(context, R.layout.dialog_adicionar_lista, null)

        val dialog = AlertDialog.Builder(activity)
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
                val listaExistente = dao.verificarListaJaExistente(text.trim().toString())

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
        dao.inserir(Lista(nome = nomeLista))

        adapter.preencherLista(dao.obterListas())

        context.toast(getString(R.string.lista_adicionada))
    }
}