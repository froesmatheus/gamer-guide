package com.matheusfroes.gamerguide

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.matheusfroes.gamerguide.ui.BaseActivity
import com.matheusfroes.gamerguide.ui.calendar.CalendarFragment
import com.matheusfroes.gamerguide.ui.feed.FeedActivity
import com.matheusfroes.gamerguide.ui.gamelists.GameListsFragment
import com.matheusfroes.gamerguide.ui.mygames.MyGamesFragment
import com.matheusfroes.gamerguide.ui.settings.SettingsActivity
import com.matheusfroes.gamerguide.ui.statistics.StatisticsFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.toolbar.*


class MainActivity : BaseActivity() {
    lateinit var meusJogos: MyGamesFragment
    lateinit var calendario: CalendarFragment
    lateinit var feed: FeedActivity
    lateinit var listas: GameListsFragment
    lateinit var estatisticas: StatisticsFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        calendario = CalendarFragment()
        feed = FeedActivity()
        listas = GameListsFragment()
        estatisticas = StatisticsFragment()

        setSupportActionBar(toolbar)

        bottom_navigation.disableShiftMode()

        bottom_navigation.selectedItemId = R.id.menu_feed
        changeFragment("FEED")

        bottom_navigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menu_jogos -> {
                    changeFragment("MEUS_JOGOS")
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.menu_calendario -> {
                    changeFragment("CALENDARIO")
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.menu_feed -> {
                    changeFragment("FEED")
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.menu_listas -> {
                    changeFragment("LISTAS")
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.menu_estatisticas -> {
                    changeFragment("ESTATISTICAS")
                    return@setOnNavigationItemSelectedListener true
                }
            }



            false
        }
    }

    private fun changeFragment(tag: String) {
        var frag = supportFragmentManager.findFragmentByTag(tag)

        if (frag == null) {
            when (tag) {
                "MEUS_JOGOS" -> {
                    frag = MyGamesFragment()
                }
                "CALENDARIO" -> {
                    frag = calendario
                }
                "FEED" -> {
                    frag = feed
                }
                "LISTAS" -> {
                    frag = listas
                }
                "ESTATISTICAS" -> {
                    frag = estatisticas
                }
            }
        }

        currentScreen(tag)

        supportFragmentManager
                .beginTransaction()
                .replace(R.id.content, frag, tag)
                .commit()

        supportFragmentManager.executePendingTransactions()
    }

    fun currentScreen(tag: String) {
        val toolbarTitle = when (tag) {
            "MEUS_JOGOS" -> {
                "Jogos"
            }
            "CALENDARIO" -> {
                "Calendário"
            }
            "FEED" -> {
                "Feed de Notícias"
            }
            "LISTAS" -> {
                "Listas"
            }
            "ESTATISTICAS" -> {
                "Estatísticas"
            }
            else -> "Jogos"

        }

        val tabLayoutVisibility = if (tag == "MEUS_JOGOS") View.VISIBLE else View.GONE
        tabLayout.visibility = tabLayoutVisibility

        title = toolbarTitle
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_principal, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.navConfiguracoes -> {
                startActivity(Intent(this, SettingsActivity::class.java))
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }
}