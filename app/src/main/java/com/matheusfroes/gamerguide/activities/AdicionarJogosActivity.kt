package com.matheusfroes.gamerguide.activities

import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import com.matheusfroes.gamerguide.R
import com.matheusfroes.gamerguide.adapters.EscolherPlataformaAdapter
import com.matheusfroes.gamerguide.adapters.JogosNaoTerminadosAdapter
import kotlinx.android.synthetic.main.activity_adicionar_jogos.*


class AdicionarJogosActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_adicionar_jogos)

        val adapter = JogosNaoTerminadosAdapter(this)

        rvJogos.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rvJogos.adapter = adapter


        etPlataformas.setOnClickListener { dialogEscolherPlataformas() }
        etPlataformas.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus) dialogEscolherPlataformas()
        }
    }

    private fun dialogEscolherPlataformas() {
        val plataformas = listOf("Xbox 360", "Xbox One", "Playstation 3", "Playstation 4")

        val dialog = AlertDialog.Builder(this)
                .setAdapter(EscolherPlataformaAdapter(this, plataformas)) { dialogInterface, i ->

                }
                .setPositiveButton("Confirmar", null)
                .setNegativeButton("Cancelar", null)
                .create()

        dialog.show()
    }
}
