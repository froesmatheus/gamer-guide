package com.matheusfroes.gamerguide.activities

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.matheusfroes.gamerguide.EndlessScrollListener
import com.matheusfroes.gamerguide.R
import com.matheusfroes.gamerguide.adapters.LancamentosAdapter
import com.matheusfroes.gamerguide.api.ApiService
import com.matheusfroes.gamerguide.db.PlataformasDAO
import com.matheusfroes.gamerguide.normalizarDadosLancamentos
import kotlinx.android.synthetic.main.activity_calendario.*
import kotlinx.android.synthetic.main.toolbar.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*

/**
 * Created by matheusfroes on 04/10/2017.
 */
class CalendarioActivity : BaseActivityDrawer() {
    private val plataformasDAO: PlataformasDAO by lazy {
        PlataformasDAO(this)
    }

    val lancamentosAdapter: LancamentosAdapter by lazy {
        LancamentosAdapter(this)
    }
    var nextPage = ""
    val retrofit = Retrofit.Builder()
            .baseUrl(ApiService.URL_BASE)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendario)
        setSupportActionBar(toolbar)
        configurarDrawer()
        title = getString(R.string.calendario)


        val dataAtual = Calendar.getInstance().timeInMillis

        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rvLancamentos.layoutManager = layoutManager
        rvLancamentos.adapter = lancamentosAdapter
        rvLancamentos.addOnScrollListener(object : EndlessScrollListener(layoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                obterLancamentos(dataAtual)
            }
        })

        obterLancamentos(dataAtual)
    }

    private fun obterLancamentos(dataAtual: Long) {
        val service = retrofit.create(ApiService::class.java)

        doAsync {
            val call = if (nextPage.isEmpty()) {
                service.obterUltimosLancamentos(dataAtual)
            } else {
                service.proximaPaginaLancamentos(nextPage)
            }

            val response = call.execute()

            nextPage = response.headers()["X-Next-Page"].toString()
            if (response.isSuccessful) {
                val lancamentos = response.body()?.map { resp ->
                    normalizarDadosLancamentos(resp, plataformasDAO)
                }?.distinctBy { it.game.nome }?.toMutableList()

                uiThread {
                    lancamentosAdapter.preencherLista(lancamentos!!)
                }
            }
        }
    }


    override fun onStart() {
        super.onStart()

        setDrawerSelectedItem(BaseActivityDrawer.CALENDARIO_IDENTIFIER)
    }
}