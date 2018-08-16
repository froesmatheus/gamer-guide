package com.matheusfroes.gamerguide.ui.gamedetails

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.design.widget.CoordinatorLayout
import android.support.design.widget.Snackbar
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import com.matheusfroes.gamerguide.JogoAdicionadoRemovidoEvent
import com.matheusfroes.gamerguide.R
import com.matheusfroes.gamerguide.appInjector
import com.matheusfroes.gamerguide.data.model.Game
import com.matheusfroes.gamerguide.data.model.InsertType
import com.matheusfroes.gamerguide.obterImagemJogoCapa
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_detalhes_jogo.*
import kotlinx.android.synthetic.main.dialog_remover_jogo.view.*
import org.greenrobot.eventbus.EventBus
import javax.inject.Inject


class GameDetailsActivity : AppCompatActivity() {
    private val preferences: SharedPreferences by lazy {
        getSharedPreferences("PREFERENCES", Context.MODE_PRIVATE)
    }
    private var jogoSalvo = false

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: GameDetailsViewModel

    private var gameId = 0L
    private val game by lazy { intent.getSerializableExtra("jogo") as Game }

    override fun onCreate(savedInstanceState: Bundle?) {
        val appTheme = preferences.getString("APP_THEME", "DEFAULT")
        val theme = if (appTheme == "DEFAULT") R.style.AppTheme_DetalhesJogo else R.style.AppTheme_DetalhesJogo_OLED
        setTheme(theme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalhes_jogo)
        appInjector.inject(this)
        setSupportActionBar(toolbar)

        intent ?: return

        gameId = intent.getLongExtra("id_jogo", 0L)
        viewModel = ViewModelProviders.of(this, viewModelFactory)[GameDetailsViewModel::class.java]

        if (gameId == 0L) {
            viewModel.game.postValue(game)
            gameId = game.id
        } else {
            viewModel.gameId = gameId
        }

        viewModel.currentAppTheme.value = preferences.getString("APP_THEME", "DEFAULT")

        appBar.post {
            val heightPx = ivCapaJogo.height
            setAppBarOffset(heightPx / 2)
        }

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = ""

        viewModel.game.observe(this, Observer { game ->
            if (game != null) updateUI(game)
        })
    }

    private fun updateUI(game: Game) {
        jogoSalvo = viewModel.getGameByInsertType(game.id) != null
        if (jogoSalvo) {
            fabAdicinarJogo.setImageResource(R.drawable.ic_adicionado)
        } else {
            fabAdicinarJogo.setImageResource(R.drawable.ic_adicionar)
        }

        if (game.coverImage.isNotEmpty()) {
            Picasso
                    .with(this)
                    .load(obterImagemJogoCapa(game.coverImage))
                    .into(ivCapaJogo)
        } else {
            appBar.setExpanded(false, false)
        }

        if (jogoSalvo) {
            fabAdicinarJogo.setImageResource(R.drawable.ic_adicionado)
        }

        fabAdicinarJogo.setOnClickListener {

            if (jogoSalvo) {
                dialogRemoverJogo(viewModel.gameId)
            } else {
                val snackbar = Snackbar.make(coordinatorLayout, getString(R.string.jogo_adicionado), Snackbar.LENGTH_LONG)
                viewModel.addGame(game)
                jogoSalvo = true
                EventBus.getDefault().postSticky(JogoAdicionadoRemovidoEvent())
                fabAdicinarJogo.setImageResource(R.drawable.ic_adicionado)
                snackbar.show()
            }
        }

        val tabAdapter = GameDetailsTabAdapter(this, supportFragmentManager)
        viewPager.adapter = tabAdapter
        tabLayout.setupWithViewPager(viewPager)
        viewPager.setCurrentItem(0, false)

        if (game.videos.isEmpty()) {
            tabAdapter.removeTabPage(1)
        }
    }

    private fun dialogRemoverJogo(jogoId: Long) {
        val view = LayoutInflater.from(this).inflate(R.layout.dialog_remover_jogo, null, false)

        val gameIsInGameLists = viewModel.gameIsInGameLists(jogoId)

        if (!gameIsInGameLists) {
            view.chkRemoverDasListas.visibility = View.GONE
        }
        val dialog = AlertDialog.Builder(this)
                .setView(view)
                .setPositiveButton(getString(R.string.confirmar)) { _, _ ->
                    val removerDasListas = view.chkRemoverDasListas.isChecked

                    val jogo = viewModel.getGameByInsertType(jogoId)

                    if (removerDasListas || !gameIsInGameLists) {
                        viewModel.removeGameFromLists(jogoId)
                        viewModel.removeGame(jogoId)
                    } else {
                        jogo?.insertType = InsertType.INSERT_TO_LIST
                        viewModel.updateGame(jogo!!)
                    }

                    jogoSalvo = false
                    EventBus.getDefault().postSticky(JogoAdicionadoRemovidoEvent())
                    fabAdicinarJogo.setImageResource(R.drawable.ic_adicionar)
                    val snackbar = Snackbar.make(coordinatorLayout, getString(R.string.jogo_removido), Snackbar.LENGTH_LONG)
                    snackbar.show()
                }
                .setNegativeButton(getString(R.string.cancelar), null)
                .create()

        dialog.show()
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
