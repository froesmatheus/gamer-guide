package com.matheusfroes.gamerguide.activities

import android.os.Bundle
import android.support.design.widget.CoordinatorLayout
import android.support.v7.app.AppCompatActivity
import com.matheusfroes.gamerguide.R
import com.matheusfroes.gamerguide.adapters.DetalhesJogosFragmentAdapter
import com.matheusfroes.gamerguide.adicionarSchemaUrl
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_detalhes_jogo.*


class DetalhesJogoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalhes_jogo)
        setSupportActionBar(toolbar)

        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.CollapsedAppBarText)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        viewPager.adapter = DetalhesJogosFragmentAdapter(supportFragmentManager)
        tabLayout.setupWithViewPager(viewPager)

        supportActionBar?.title = ""

        var cover = "//images.igdb.com/igdb/image/upload/t_720p/e7vzqpimo6pwovewqqli.jpg"

        cover = adicionarSchemaUrl(cover)

        Picasso
                .with(this)
                .load(cover)
                .into(ivCapaJogo)

        //lockAppBarClosed()
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
