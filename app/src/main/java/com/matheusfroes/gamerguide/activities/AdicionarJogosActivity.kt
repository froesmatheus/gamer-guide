package com.matheusfroes.gamerguide.activities

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import com.matheusfroes.gamerguide.JogoAdicionadoRemovidoEvent
import com.matheusfroes.gamerguide.R
import com.matheusfroes.gamerguide.adapters.AdicionarJogosAdapter
import com.matheusfroes.gamerguide.db.JogosDAO
import com.matheusfroes.gamerguide.esconderTeclado
import com.matheusfroes.gamerguide.models.Jogo
import kotlinx.android.synthetic.main.activity_adicionar_jogos.*
import org.greenrobot.eventbus.EventBus
import org.jetbrains.anko.toast


class AdicionarJogosActivity : AppCompatActivity() {
    val adapter: AdicionarJogosAdapter by lazy {
        AdicionarJogosAdapter(this)
    }
    private val viewModel: AdicionarJogosViewModel by lazy {
        ViewModelProviders.of(this).get(AdicionarJogosViewModel::class.java)
    }
    private val jogosDAO: JogosDAO by lazy {
        JogosDAO(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_adicionar_jogos)

        rvJogos.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rvJogos.adapter = adapter

        adapter.setOnMenuItemClickListener(object : AdicionarJogosAdapter.OnAdicionarJogoListener {
            override fun onMenuItemClick(jogo: Jogo) {
                toast(getString(R.string.jogo_adicionado))
                jogosDAO.inserir(jogo)
                EventBus.getDefault().postSticky(JogoAdicionadoRemovidoEvent())
            }
        })


        viewModel.listaPesquisas.observe(this, Observer { listaJogos ->
            adapter.preencherLista(listaJogos!!)
            rvJogos.smoothScrollToPosition(0)
        })

        // Obter jogos mais populares do momento
        viewModel.obterLancamentos()

        etNomeJogo.setOnEditorActionListener { _, _, _ ->
            esconderTeclado(this)
            viewModel.pesquisarJogos(query = etNomeJogo.text.toString())
            true
        }
    }
}
