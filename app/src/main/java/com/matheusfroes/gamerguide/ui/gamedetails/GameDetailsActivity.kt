package com.matheusfroes.gamerguide.ui.gamedetails

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.design.widget.CoordinatorLayout
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import com.matheusfroes.gamerguide.*
import com.matheusfroes.gamerguide.data.model.Game
import com.matheusfroes.gamerguide.ui.addgamedialog.AddGameDialog
import com.matheusfroes.gamerguide.ui.gamedetails.gameinfo.GameInfoFragment
import com.matheusfroes.gamerguide.ui.gamedetails.livestream.StreamsFragment
import com.matheusfroes.gamerguide.ui.gamedetails.video.VideosFragment
import com.matheusfroes.gamerguide.ui.removegamedialog.RemoveGameDialog
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_detalhes_jogo.*
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
                openRemoveGameDialog(gameId)
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
                jogoSalvo = true
                fabAdicinarJogo.setImageResource(R.drawable.ic_adicionado)
                snack(R.string.jogo_adicionado)
            }
        }
    }

    private fun openRemoveGameDialog(gameId: Long) {
        val removeGameDialog = RemoveGameDialog.newInstance(gameId)
        removeGameDialog.show(supportFragmentManager, "REMOVE_GAME_DIALOG")

        removeGameDialog.gameRemovedEvent = { gameRemoved ->
            if (gameRemoved) {
                jogoSalvo = false
                fabAdicinarJogo.setImageResource(R.drawable.ic_adicionar)
                snack(R.string.jogo_removido)
            } else {
                snack(R.string.game_removed_error)
            }
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
