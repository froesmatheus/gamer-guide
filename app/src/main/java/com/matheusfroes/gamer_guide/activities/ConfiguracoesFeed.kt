package com.matheusfroes.gamer_guide.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.matheusfroes.gamer_guide.R
import com.matheusfroes.gamer_guide.adapters.FonteNoticiasAdapter
import com.matheusfroes.gamer_guide.models.FonteNoticia
import kotlinx.android.synthetic.main.activity_configuracoes_feed.*

class ConfiguracoesFeed : AppCompatActivity() {

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

        lvFonteNoticias.adapter = adapter
    }
}
