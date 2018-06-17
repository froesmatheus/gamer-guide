package com.matheusfroes.gamerguide.ui.calendar

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.matheusfroes.gamerguide.*
import com.matheusfroes.gamerguide.network.ApiService
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.activity_calendario.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.util.*

class CalendarFragment : DaggerFragment() {
    private val lancamentosAdapter: LancamentosAdapter by lazy {
        LancamentosAdapter(context())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
            inflater.inflate(R.layout.activity_calendario, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val dataAtual = Calendar.getInstance().timeInMillis

        val layoutManager = LinearLayoutManager(context(), LinearLayoutManager.VERTICAL, false)
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

    override fun onResume() {
        super.onResume()

        (activity() as MainActivity).currentScreen(R.id.menu_calendario)
    }
}