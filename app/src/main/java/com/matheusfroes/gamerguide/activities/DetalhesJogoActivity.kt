package com.matheusfroes.gamerguide.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.matheusfroes.gamerguide.R
import kotlinx.android.synthetic.main.activity_detalhes_jogo.*

class DetalhesJogoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalhes_jogo)
        setSupportActionBar(toolbar)

        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.CollapsedAppBarText)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }
}
