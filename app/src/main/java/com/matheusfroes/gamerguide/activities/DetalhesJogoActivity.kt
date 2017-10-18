package com.matheusfroes.gamerguide.activities

import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.design.widget.CoordinatorLayout
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import com.matheusfroes.gamerguide.JogoAdicionadoRemovidoEvent
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
    private val preferences: SharedPreferences by lazy {
        getSharedPreferences("PREFERENCES", Context.MODE_PRIVATE)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        val appTheme = preferences.getString("APP_THEME", "DEFAULT")
        val theme = if (appTheme == "DEFAULT") R.style.AppTheme_DetalhesJogo else R.style.AppTheme_DetalhesJogo_OLED
        setTheme(theme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalhes_jogo)
        setSupportActionBar(toolbar)

        //collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.CollapsedAppBarText)


        appBar.post {
            val heightPx = ivCapaJogo.height
            setAppBarOffset(heightPx / 2)
        }


        intent ?: return

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = ""

        val jogo: Jogo
        var jogoId = intent.getLongExtra("id_jogo", 0L)
        jogo = if (jogoId != 0L) {
            jogosDAO.obterJogo(jogoId)!!
        } else {
            intent.getSerializableExtra("jogo") as Jogo
        }
        jogoId = jogo.id
        var jogoSalvo = jogosDAO.obterJogo(jogo.id) != null
        if (jogoSalvo) {
            fabAdicinarJogo.setImageResource(R.drawable.ic_adicionado)
        } else {
            fabAdicinarJogo.setImageResource(R.drawable.ic_adicionar)
        }

        viewModel.jogo.value = jogo


        val tabAdapter = DetalhesJogosFragmentAdapter(this, supportFragmentManager)
        viewPager.adapter = tabAdapter
        tabLayout.setupWithViewPager(viewPager)
        viewPager.setCurrentItem(0, false)

        if (viewModel.jogo.value?.videos?.size == 0) {
            tabAdapter.removeTabPage(1)
        }

        if (jogo.imageCapa.isNotEmpty()) {
            Picasso
                    .with(this)
                    .load(obterImagemJogoCapa(jogo.imageCapa))
                    .into(ivCapaJogo)
        } else {
            appBar.setExpanded(false, false)
        }

        if (jogoSalvo) {
            fabAdicinarJogo.setImageResource(R.drawable.ic_adicionado)
        }

        fabAdicinarJogo.setOnClickListener {
            val snackbar: Snackbar

            if (jogoSalvo) {
                snackbar = Snackbar.make(coordinatorLayout, getString(R.string.jogo_removido), Snackbar.LENGTH_LONG)
                jogosDAO.remover(jogoId)
                jogoSalvo = false
                EventBus.getDefault().postSticky(JogoAdicionadoRemovidoEvent())
                fabAdicinarJogo.setImageResource(R.drawable.ic_adicionar)
            } else {
                snackbar = Snackbar.make(coordinatorLayout, getString(R.string.jogo_adicionado), Snackbar.LENGTH_LONG)
                jogosDAO.inserir(jogo)
                jogoSalvo = true
                EventBus.getDefault().postSticky(JogoAdicionadoRemovidoEvent())
                fabAdicinarJogo.setImageResource(R.drawable.ic_adicionado)
            }

            snackbar.show()
        }
    }

    private fun setAppBarOffset(offsetPx: Int) {
        val params = appBar.layoutParams as CoordinatorLayout.LayoutParams
        val behavior = params.behavior as AppBarLayout.Behavior?
        behavior!!.onNestedPreScroll(coordinatorLayout, appBar, toolbar, 0, offsetPx, intArrayOf(0, 0), 0)
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
