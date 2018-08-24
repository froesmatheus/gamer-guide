package com.matheusfroes.gamerguide.ui

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.Menu
import android.view.MenuItem
import com.matheusfroes.gamerguide.R
import com.matheusfroes.gamerguide.extra.consume
import com.matheusfroes.gamerguide.extra.disableShiftMode
import com.matheusfroes.gamerguide.extra.inTransaction
import com.matheusfroes.gamerguide.ui.feed.FeedFragment
import com.matheusfroes.gamerguide.ui.gamelists.GameListsFragment
import com.matheusfroes.gamerguide.ui.mygames.MyGamesFragment
import com.matheusfroes.gamerguide.ui.settings.SettingsActivity
import com.matheusfroes.gamerguide.ui.statistics.StatisticsFragment
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : BaseActivity() {
    companion object {
        private const val FRAGMENT_ID = R.id.fragment_container
    }

    private lateinit var currentFragment: Fragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottomNavigationView.disableShiftMode()

        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menu_jogos -> consume { replaceFragment(MyGamesFragment()) }
//                R.id.menu_calendario -> consume { replaceFragment(CalendarFragment()) }
                R.id.menu_feed -> consume { replaceFragment(FeedFragment()) }
                R.id.menu_listas -> consume { replaceFragment(GameListsFragment()) }
                R.id.menu_estatisticas -> consume { replaceFragment(StatisticsFragment()) }
                else -> false
            }
        }

        bottomNavigationView.setOnNavigationItemReselectedListener {}

        if (savedInstanceState == null) {
            bottomNavigationView.selectedItemId = R.id.menu_feed
        } else {
            currentFragment = supportFragmentManager.findFragmentById(FRAGMENT_ID)
                    ?: throw IllegalStateException("Activity recreated, but no fragment found!")
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_principal, menu)
        return true
    }

    private fun <F> replaceFragment(fragment: F) where F : Fragment {
        supportFragmentManager.inTransaction {
            currentFragment = fragment

            replace(FRAGMENT_ID, fragment)
        }
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