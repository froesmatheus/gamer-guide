package com.matheusfroes.gamerguide.activities

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.design.widget.CoordinatorLayout
import android.support.v7.app.AppCompatActivity
import com.matheusfroes.gamerguide.R
import com.matheusfroes.gamerguide.adapters.DetalhesJogosFragmentAdapter
import com.matheusfroes.gamerguide.models.Jogo
import com.matheusfroes.gamerguide.obterImagemJogoCapa
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_detalhes_jogo.*


class DetalhesJogoActivity : AppCompatActivity() {
    private val viewModel: DetalhesJogoViewModel by lazy {
        ViewModelProviders.of(this).get(DetalhesJogoViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalhes_jogo)
        setSupportActionBar(toolbar)

        //collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.CollapsedAppBarText)

        intent ?: return

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = ""


        val jogo = intent.getSerializableExtra("jogo") as Jogo
        viewModel.jogo.value = jogo

        viewPager.adapter = DetalhesJogosFragmentAdapter(supportFragmentManager)
        tabLayout.setupWithViewPager(viewPager)

        Picasso
                .with(this)
                .load(obterImagemJogoCapa(jogo.imageCapa))
                .into(ivCapaJogo)

    }

    fun lockAppBarClosed() {
        appBar.setExpanded(false, false)
        appBar.isActivated = false
        val lp = appBar.layoutParams as CoordinatorLayout.LayoutParams
        lp.height = resources.getDimension(R.dimen.toolar_hight).toInt()
    }

    fun unlockAppBarOpen() {
        appBar.setExpanded(true, false)
        appBar.isActivated = true
        val lp = appBar.layoutParams as CoordinatorLayout.LayoutParams
        lp.height = resources.getDimension(R.dimen.toolar_hight).toInt()
    }
}
