package com.matheusfroes.gamerguide.activities

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import com.matheusfroes.gamerguide.ListaExcluidaEvent
import com.matheusfroes.gamerguide.R
import com.matheusfroes.gamerguide.adapters.JogosListaAdapter
import kotlinx.android.synthetic.main.activity_detalhes_lista.*
import org.greenrobot.eventbus.EventBus
import org.jetbrains.anko.toast


class DetalhesListaActivity : AppCompatActivity() {
    private val viewModel: DetalhesListaViewModel by lazy {
        ViewModelProviders.of(this).get(DetalhesListaViewModel::class.java)
    }
    private val adapter: JogosListaAdapter by lazy {
        JogosListaAdapter(this)
    }

    private var listaId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalhes_lista)

        intent ?: return


        listaId = intent.getIntExtra("lista_id", 0)

        val lista = viewModel.getLista(listaId)!!

        rvJogosLista.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rvJogosLista.emptyView = layoutEmpty
        rvJogosLista.adapter = adapter

        adapter.preencherLista(lista.jogos)
        title = lista.nome
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
                EventBus.getDefault().postSticky(ListaExcluidaEvent())
                finish()
            }
        }

        return super.onOptionsItemSelected(item)
    }
}
