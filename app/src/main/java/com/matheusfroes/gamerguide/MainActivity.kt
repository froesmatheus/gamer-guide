package com.matheusfroes.gamerguide

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.matheusfroes.gamerguide.ui.calendario.CalendarioActivity
import com.matheusfroes.gamerguide.ui.configuracoes.ConfiguracoesActivity
import com.matheusfroes.gamerguide.ui.estatisticas.EstatisticasActivity
import com.matheusfroes.gamerguide.ui.feed.FeedActivity
import com.matheusfroes.gamerguide.ui.gamelists.GameListsFragment
import com.matheusfroes.gamerguide.ui.meusjogos.MeusJogosActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.toolbar.*


class MainActivity : AppCompatActivity() {
    lateinit var meusJogos: MeusJogosActivity
    lateinit var calendario: CalendarioActivity
    lateinit var feed: FeedActivity
    lateinit var listas: GameListsFragment
    lateinit var estatisticas: EstatisticasActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        calendario = CalendarioActivity()
        feed = FeedActivity()
        listas = GameListsFragment()
        estatisticas = EstatisticasActivity()

        setSupportActionBar(toolbar)
//        supportActionBar?.setDisplayShowTitleEnabled(false)

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
                    frag = MeusJogosActivity()
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
        when (item.itemId)  {
            R.id.navConfiguracoes -> {
                startActivity(Intent(this, ConfiguracoesActivity::class.java))
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }
}