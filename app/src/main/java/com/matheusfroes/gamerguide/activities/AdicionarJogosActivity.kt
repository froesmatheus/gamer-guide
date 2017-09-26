package com.matheusfroes.gamerguide.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import com.matheusfroes.gamerguide.R
import com.matheusfroes.gamerguide.adapters.MeusJogosAdapter
import kotlinx.android.synthetic.main.activity_adicionar_jogos.*


class AdicionarJogosActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_adicionar_jogos)

        val adapter = MeusJogosAdapter(this)

        rvJogos.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rvJogos.adapter = adapter
    }
}
