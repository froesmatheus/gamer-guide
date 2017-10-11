package com.matheusfroes.gamerguide.activities

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.matheusfroes.gamerguide.R
import com.matheusfroes.gamerguide.adapters.FonteNoticiasAdapter
import com.matheusfroes.gamerguide.models.FonteNoticia
import kotlinx.android.synthetic.main.activity_configuracoes_feed.*

class ConfiguracoesFeedActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_configuracoes_feed)

        val fontesNoticias = listOf(
                FonteNoticia("Tecmundo", "http://games.tecmundo.com.br/"),
                FonteNoticia("UOL Jogos", "https://jogos.uol.com.br/"),
                FonteNoticia("Eurogamer", "http://www.eurogamer.pt/"),
                FonteNoticia("IGN Brasil", "http://br.ign.com/")
        )

        val adapter = FonteNoticiasAdapter(this, fontesNoticias)

        rvFonteNoticias.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rvFonteNoticias.adapter = adapter
        rvFonteNoticias.setHasFixedSize(true)
    }
}
