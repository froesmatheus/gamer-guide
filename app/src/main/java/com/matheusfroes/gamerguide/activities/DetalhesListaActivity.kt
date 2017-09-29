package com.matheusfroes.gamerguide.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import com.matheusfroes.gamerguide.R
import com.matheusfroes.gamerguide.adapters.JogosListaAdapter
import com.matheusfroes.gamerguide.db.ListasDAO
import kotlinx.android.synthetic.main.activity_detalhes_lista.*


class DetalhesListaActivity : AppCompatActivity() {
    private val dao: ListasDAO by lazy {
        ListasDAO(this)
    }
    private val adapter: JogosListaAdapter by lazy {
        JogosListaAdapter(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalhes_lista)

        intent ?: return

        val lista = dao.obterLista(intent.getIntExtra("lista_id", 0))!!
        rvJogosLista.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rvJogosLista.adapter = adapter

        adapter.preencherLista(lista.jogos)
        title = lista.nome
    }
}
