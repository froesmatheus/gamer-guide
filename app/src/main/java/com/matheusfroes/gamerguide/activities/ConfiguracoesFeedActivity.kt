package com.matheusfroes.gamerguide.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import com.matheusfroes.gamerguide.R
import com.matheusfroes.gamerguide.adapters.FonteNoticiasAdapter
import com.matheusfroes.gamerguide.db.FonteNoticiasDAO
import kotlinx.android.synthetic.main.activity_configuracoes_feed.*

class ConfiguracoesFeedActivity : AppCompatActivity() {
    private val fonteNoticasDAO: FonteNoticiasDAO by lazy {
        FonteNoticiasDAO(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_configuracoes_feed)

        val fontesNoticias = fonteNoticasDAO.obterFonteNoticias()
        val adapter = FonteNoticiasAdapter(this, fontesNoticias)


        adapter.setAlterarStatusFonteNoticiaListener(object : FonteNoticiasAdapter.AlterarStatusFonteNoticiaListener {
            override fun alterarStatus(fonteId: Int, ativo: Boolean) {
                fonteNoticasDAO.alterarStatusFonteNoticia(fonteId, ativo)
            }
        })

        rvFonteNoticias.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rvFonteNoticias.adapter = adapter
        rvFonteNoticias.setHasFixedSize(true)
    }
}
