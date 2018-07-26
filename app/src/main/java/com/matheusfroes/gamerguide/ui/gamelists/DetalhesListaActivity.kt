package com.matheusfroes.gamerguide.ui.gamelists

import android.arch.lifecycle.ViewModelProvider
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.matheusfroes.gamerguide.ListaExcluidaEditadaEvent
import com.matheusfroes.gamerguide.R
import com.matheusfroes.gamerguide.appInjector
import com.matheusfroes.gamerguide.data.models.Lista
import com.matheusfroes.gamerguide.ui.BaseActivity
import com.matheusfroes.gamerguide.ui.adicionarjogos.AdicionarJogosActivity
import kotlinx.android.synthetic.main.activity_detalhes_lista.*
import kotlinx.android.synthetic.main.dialog_adicionar_lista.view.*
import kotlinx.android.synthetic.main.toolbar.*
import org.greenrobot.eventbus.EventBus
import org.jetbrains.anko.toast
import javax.inject.Inject


class DetalhesListaActivity : BaseActivity() {
    private val adapter: JogosListaAdapter by lazy {
        JogosListaAdapter()
    }


    var lista: Lista? = null

    private var listaId = 0

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: DetalhesListaViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalhes_lista)
        setSupportActionBar(toolbar)
        appInjector.inject(this)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        intent ?: return




        listaId = intent.getIntExtra("lista_id", 0)

        lista = viewModel.getLista(listaId)!!

        rvJogosLista.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rvJogosLista.emptyView = layoutEmpty
        rvJogosLista.adapter = adapter

        adapter.preencherLista(lista?.jogos!!)
        title = lista?.nome


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
                viewModel.excluirLista(listaId)
                toast(getString(R.string.lista_excluida))
                EventBus.getDefault().postSticky(ListaExcluidaEditadaEvent())
                finish()
            }
            R.id.navEditarLista -> {
                dialogEditarLista()
            }
        }

        return super.onOptionsItemSelected(item)
    }

    private fun dialogEditarLista() {
        val view = View.inflate(this, R.layout.dialog_adicionar_lista, null)

        view.etNomeLista.setText(title)

        val dialog = AlertDialog.Builder(this)
                .setView(view)
                .setPositiveButton(getString(R.string.atualizar)) { _, _ ->
                    editarLista()
                }
                .setNegativeButton(getString(R.string.cancelar), null)
                .create()

        dialog.show()
        val botaoAdicionar = dialog.getButton(AlertDialog.BUTTON_POSITIVE)

        botaoAdicionar.isEnabled = false

        view.etNomeLista.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {}

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(text: CharSequence, p1: Int, p2: Int, p3: Int) {
                val listaExistente = dao.verificarListaJaExistente(text.trim().toString())

                if (listaExistente && text.toString() != title) {
                    view.tilNomeLista.isErrorEnabled = true
                    view.tilNomeLista.error = getString(R.string.msg_lista_existente)
                } else {
                    view.tilNomeLista.error = null
                    view.tilNomeLista.isErrorEnabled = false

                    lista?.nome = text.toString()
                }

                botaoAdicionar.isEnabled = text.isNotEmpty() && !listaExistente
            }
        })
    }

    private fun editarLista() {
        dao.editar(lista!!)
        recreate()
        EventBus.getDefault().postSticky(ListaExcluidaEditadaEvent())
        toast(getString(R.string.lista_atualizada))
    }
}

