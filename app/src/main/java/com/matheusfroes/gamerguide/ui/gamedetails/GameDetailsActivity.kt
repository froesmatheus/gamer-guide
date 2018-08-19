package com.matheusfroes.gamerguide.ui.gamedetails

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.design.widget.CoordinatorLayout
import android.support.design.widget.Snackbar
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import com.matheusfroes.gamerguide.*
import com.matheusfroes.gamerguide.data.model.Game
import com.matheusfroes.gamerguide.data.model.InsertType
import com.matheusfroes.gamerguide.ui.addgamedialog.AddGameDialog
import com.matheusfroes.gamerguide.ui.gamedetails.gameinfo.GameInfoFragment
import com.matheusfroes.gamerguide.ui.gamedetails.livestream.StreamsFragment
import com.matheusfroes.gamerguide.ui.gamedetails.video.VideosFragment
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_detalhes_jogo.*
import kotlinx.android.synthetic.main.dialog_remover_jogo.view.*
import javax.inject.Inject


class GameDetailsActivity : AppCompatActivity() {
    private var jogoSalvo = false

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject
    lateinit var userPreferences: UserPreferences
    private lateinit var viewModel: GameDetailsViewModel

    private var gameId = 0L
    private val game by lazy { intent.getSerializableExtra("jogo") as Game }

    override fun onCreate(savedInstanceState: Bundle?) {
        appInjector.inject(this)
        setTheme(userPreferences.getGameDetailsScreenTheme())
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalhes_jogo)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = ""

        intent ?: return

        gameId = intent.getLongExtra("id_jogo", 0L)
        viewModel = viewModelProvider(viewModelFactory)

        /* Parent activity may send a gameId if the game is already added or a Game object if
        the game comes from the AddGamesActivity
        */
        if (gameId == 0L) {
            viewModel.game.postValue(game)
            gameId = game.id
        } else {
            viewModel.gameId = gameId
        }

        appBar.post {
            val heightPx = ivCapaJogo.height
            setAppBarOffset(heightPx / 2)
        }

        viewModel.game.observe(this, Observer { game ->
            if (game != null) updateUI(game)
        })
    }

    private fun updateUI(game: Game) {
        jogoSalvo = viewModel.getGame(game.id) != null
        fabAdicinarJogo
                .setImageResource(
                        if (jogoSalvo) R.drawable.ic_adicionado else R.drawable.ic_adicionar
                )

        fabAdicinarJogo.setOnClickListener {
            if (jogoSalvo) {
                dialogRemoverJogo(viewModel.gameId)
            } else {
                openAddGameDialog(game)
            }
        }

        if (game.coverImage.isNotEmpty()) {
            Picasso
                    .with(this)
                    .load(obterImagemJogoCapa(game.coverImage))
                    .into(ivCapaJogo)
        } else {
            appBar.setExpanded(false, false)
        }

        val tabAdapter = GameDetailsTabAdapter(supportFragmentManager)
        viewPager.adapter = tabAdapter
        tabLayout.setupWithViewPager(viewPager)
        viewPager.setCurrentItem(0, false)

        if (game.videos.isEmpty()) {
            tabAdapter.removeTabPage(1)
        }
    }

    private fun openAddGameDialog(game: Game) {
        val addGameDialog = AddGameDialog.newInstance(game)
        addGameDialog.show(supportFragmentManager, "ADD_GAME_DIALOG")

        addGameDialog.gameAddedEvent = { gameAdded ->
            if (gameAdded) {
                val snackbar = Snackbar.make(coordinatorLayout, getString(R.string.jogo_adicionado), Snackbar.LENGTH_LONG)
                jogoSalvo = true
                fabAdicinarJogo.setImageResource(R.drawable.ic_adicionado)
                snackbar.show()
            }
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

    inner class GameDetailsTabAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {
        override fun getItem(position: Int) = GAMES_DETAILS_PAGES[position]()

        override fun getCount() = GAMES_DETAILS_TITLES.size

        override fun getPageTitle(position: Int): String = resources.getString(GAMES_DETAILS_TITLES[position])

        fun removeTabPage(position: Int) {
            if (!GAMES_DETAILS_PAGES.isEmpty() && position < GAMES_DETAILS_PAGES.size) {
                GAMES_DETAILS_PAGES.removeAt(position)
                GAMES_DETAILS_TITLES.removeAt(position)
                notifyDataSetChanged()
            }
        }

        private val GAMES_DETAILS_TITLES = mutableListOf(
                R.string.tab_informacoes,
                R.string.tab_videos,
                R.string.streams
        )
        private val GAMES_DETAILS_PAGES = mutableListOf(
                { GameInfoFragment() },
                { VideosFragment() },
                { StreamsFragment() }
        )
    }
}
