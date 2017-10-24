package com.matheusfroes.gamerguide.activities

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.TextInputEditText
import android.support.v7.app.AlertDialog
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import com.matheusfroes.gamerguide.ListaExcluidaEditadaEvent
import com.matheusfroes.gamerguide.R
import com.matheusfroes.gamerguide.adapters.ListasAdapter
import com.matheusfroes.gamerguide.db.ListasDAO
import com.matheusfroes.gamerguide.models.Lista
import kotlinx.android.synthetic.main.activity_listas.*
import kotlinx.android.synthetic.main.dialog_adicionar_lista.view.*
import kotlinx.android.synthetic.main.fab.*
import kotlinx.android.synthetic.main.toolbar.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.jetbrains.anko.toast


/**
 * Created by matheus_froes on 19/09/2017.
 */
class ListasActivity : BaseActivityDrawer() {
    val adapter: ListasAdapter by lazy {
        ListasAdapter(this)
    }
    private val dao: ListasDAO by lazy {
        ListasDAO(this)
    }
    var etNomeLista: TextInputEditText? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_listas)
        setSupportActionBar(toolbar)
        configurarDrawer()
        title = "Listas"


        fab.setOnClickListener { dialogAdicionarLista() }

        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rvListas.layoutManager = layoutManager
        rvListas.emptyView = layoutEmpty

        val mDividerItemDecoration = DividerItemDecoration(
                rvListas.context,
                layoutManager.orientation
        )
        rvListas.addItemDecoration(mDividerItemDecoration)

        rvListas.adapter = adapter

        adapter.preencherLista(dao.obterListas())

        adapter.setOnListaClickListener(object : ListasAdapter.OnListaClickListener {
            override fun onListaClick(listaId: Int) {
                val intent = Intent(applicationContext, DetalhesListaActivity::class.java)
                intent.putExtra("lista_id", listaId)
                startActivity(intent)
            }
        })
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    fun onMessageEvent(event: ListaExcluidaEditadaEvent) {
        adapter.preencherLista(dao.obterListas())
    }

    override fun onStart() {
        super.onStart()

        setDrawerSelectedItem(BaseActivityDrawer.LISTAS_IDENTIFIER)

        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()


        EventBus.getDefault().unregister(this)
    }

    private fun dialogAdicionarLista() {
        val view = View.inflate(this, R.layout.dialog_adicionar_lista, null)

        val dialog = AlertDialog.Builder(this)
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

        toast(getString(R.string.lista_adicionada))
    }
}