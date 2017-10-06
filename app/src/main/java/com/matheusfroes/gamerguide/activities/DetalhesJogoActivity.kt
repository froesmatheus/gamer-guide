package com.matheusfroes.gamerguide.activities

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.design.widget.CoordinatorLayout
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import com.matheusfroes.gamerguide.JogoAdicionadoEvent
import com.matheusfroes.gamerguide.R
import com.matheusfroes.gamerguide.adapters.DetalhesJogosFragmentAdapter
import com.matheusfroes.gamerguide.db.JogosDAO
import com.matheusfroes.gamerguide.models.Jogo
import com.matheusfroes.gamerguide.obterImagemJogoCapa
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_detalhes_jogo.*
import org.greenrobot.eventbus.EventBus


class DetalhesJogoActivity : AppCompatActivity() {
    private val viewModel: DetalhesJogoViewModel by lazy {
        ViewModelProviders.of(this).get(DetalhesJogoViewModel::class.java)
    }
    private val jogosDAO: JogosDAO by lazy {
        JogosDAO(this)
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


        val tabAdapter = DetalhesJogosFragmentAdapter(supportFragmentManager)
        viewPager.adapter = tabAdapter
        tabLayout.setupWithViewPager(viewPager)
        viewPager.setCurrentItem(1, false)

        if (viewModel.jogo.value?.videos?.size == 0) {
            tabAdapter.removeTabPage(2)
        }

        val telaOrigem = intent.getStringExtra("tela_origem")

        if (telaOrigem == "tela_adicionar") {
            tabAdapter.removeTabPage(0)
            viewPager.setCurrentItem(0, false)
        }

        Picasso
                .with(this)
                .load(obterImagemJogoCapa(jogo.imageCapa))
                .into(ivCapaJogo)

        fabAdicinarJogo.setOnClickListener {
            val snackbar = Snackbar.make(coordinatorLayout, getString(R.string.jogo_adicionado), Snackbar.LENGTH_LONG)

            jogosDAO.inserir(jogo)
            EventBus.getDefault().postSticky(JogoAdicionadoEvent())

            fabAdicinarJogo.setImageResource(R.drawable.ic_adicionado)
            snackbar.show()
        }
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
            }
        }

        return super.onOptionsItemSelected(item)
    }
}
