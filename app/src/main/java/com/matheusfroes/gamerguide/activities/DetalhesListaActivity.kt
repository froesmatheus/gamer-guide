package com.matheusfroes.gamerguide.activities

import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import com.matheusfroes.gamerguide.R
import com.matheusfroes.gamerguide.adapters.JogosListaAdapter
import kotlinx.android.synthetic.main.activity_detalhes_lista.*


class DetalhesListaActivity : AppCompatActivity() {
    private val viewModel: DetalhesListaViewModel by lazy {
        ViewModelProviders.of(this).get(DetalhesListaViewModel::class.java)
    }
    private val adapter: JogosListaAdapter by lazy {
        JogosListaAdapter(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalhes_lista)

        intent ?: return

        val listaId = intent.getIntExtra("lista_id", 0)

        val intent = Intent()
        intent.putExtra("id", 10L)

        val lista = viewModel.getLista(listaId)!!

        rvJogosLista.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rvJogosLista.adapter = adapter

        adapter.preencherLista(lista.jogos)
        title = lista.nome
    }
}
