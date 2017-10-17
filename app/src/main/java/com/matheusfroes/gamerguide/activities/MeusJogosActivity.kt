package com.matheusfroes.gamerguide.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.matheusfroes.gamerguide.BaseActivity
import com.matheusfroes.gamerguide.R
import com.matheusfroes.gamerguide.adapters.JogosFragmentAdapter
import kotlinx.android.synthetic.main.fab.*
import kotlinx.android.synthetic.main.fragment_meus_jogos.*
import kotlinx.android.synthetic.main.toolbar.*

/**
 * Created by matheus_froes on 19/09/2017.
 */
class MeusJogosActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_meus_jogos)
        setSupportActionBar(toolbar)
        configurarDrawer()

        title = "Jogos"

        val adapter = JogosFragmentAdapter(this, supportFragmentManager)
        viewPager.adapter = adapter

        tabLayout.visibility = View.VISIBLE
        tabLayout.setupWithViewPager(viewPager)
        fab.setOnClickListener {
            startActivity(Intent(this, AdicionarJogosActivity::class.java))
        }
    }
}