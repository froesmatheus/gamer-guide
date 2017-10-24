package com.matheusfroes.gamerguide.activities

import android.os.Bundle
import android.util.Log
import com.matheusfroes.gamerguide.R
import com.matheusfroes.gamerguide.api.ApiService
import com.matheusfroes.gamerguide.db.PlataformasDAO
import com.matheusfroes.gamerguide.normalizarDadosLancamentos
import kotlinx.android.synthetic.main.toolbar.*
import org.jetbrains.anko.doAsync
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendario)
        setSupportActionBar(toolbar)
        configurarDrawer()
        title = getString(R.string.calendario)


        val retrofit = Retrofit.Builder()
                .baseUrl(ApiService.URL_BASE)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        val service = retrofit.create(ApiService::class.java)

        val dataAtual = Calendar.getInstance().timeInMillis

        val call = service.obterUltimosLancamentos(dataAtual)

        doAsync {
            val response = call.execute()

            if (response.isSuccessful) {
                val lancamentos = response.body()?.map { resp ->
                    normalizarDadosLancamentos(resp, plataformasDAO)
                }

                Log.d("GAMERGUIDE", lancamentos.toString())
            }
        }


    }


    override fun onStart() {
        super.onStart()

        setDrawerSelectedItem(BaseActivityDrawer.CALENDARIO_IDENTIFIER)
    }
}