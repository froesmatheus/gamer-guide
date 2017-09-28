package com.matheusfroes.gamerguide.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import com.matheusfroes.gamerguide.R
import com.matheusfroes.gamerguide.adapters.AdicionarJogosAdapter
import com.matheusfroes.gamerguide.api.IGDBService
import com.matheusfroes.gamerguide.normalizarDadosJogo
import kotlinx.android.synthetic.main.activity_adicionar_jogos.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class AdicionarJogosActivity : AppCompatActivity() {
    private val retrofit: Retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(IGDBService.URL_BASE)
            .build()
    val adapter: AdicionarJogosAdapter by lazy {
        AdicionarJogosAdapter(this)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_adicionar_jogos)

        rvJogos.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rvJogos.adapter = adapter

        etNomeJogo.setOnEditorActionListener { textView, actionId, keyEvent ->
            pesquisarJogos()
            true
        }
    }

    private fun pesquisarJogos() {
        val service = retrofit.create(IGDBService::class.java)

        val call = service.pesquisarJogos(query = etNomeJogo.text.toString())

        doAsync {
            val response = call.execute()

            if (response.isSuccessful) {
                val listaJogos = response.body()?.map { normalizarDadosJogo(it) }

                uiThread {
                    adapter.preencherLista(listaJogos!!)
                }

            }
        }

    }
}
