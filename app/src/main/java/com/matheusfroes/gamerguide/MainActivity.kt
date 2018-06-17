package com.matheusfroes.gamerguide

import android.graphics.Typeface
import android.os.Bundle
import android.support.annotation.IdRes
import android.view.View
import com.matheusfroes.gamerguide.ui.calendar.CalendarFragment
import com.matheusfroes.gamerguide.ui.statistics.StatisticsFragment
import com.matheusfroes.gamerguide.ui.feed.FeedFragment
import com.matheusfroes.gamerguide.ui.gamelists.GameListFragment
import com.matheusfroes.gamerguide.ui.mygames.MyGamesFragment
import dagger.android.support.DaggerAppCompatActivity


class MainActivity : DaggerAppCompatActivity() {
    val meusJogosFragment = MyGamesFragment()
    val calendarioFragment = CalendarFragment()
    val feedFragment = FeedFragment()
    val listasFragment = GameListFragment()
    val estatisticasFragment = StatisticsFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bottom_sample)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        val font = Typeface.createFromAsset(assets, "productsansbold.ttf")
        toolbar.toolbar_title.typeface = font

        bottom_navigation.disableShiftMode()

        bottom_navigation.selectedItemId = R.id.menu_feed
        changeFragment(R.id.menu_feed)

        bottom_navigation.setOnNavigationItemSelectedListener { item ->
            changeFragment(item.itemId)

            true
        }
    }

    private fun changeFragment(itemId: Int) {
        val fragment = when (itemId) {
            R.id.menu_jogos -> meusJogosFragment
            R.id.menu_calendario -> calendarioFragment
            R.id.menu_feed -> feedFragment
            R.id.menu_listas -> listasFragment
            R.id.menu_estatisticas -> estatisticasFragment
            else -> MyGamesFragment()
        }

        supportFragmentManager.beginTransaction().replace(R.id.content, fragment).commit()
        supportFragmentManager.executePendingTransactions()
    }

    fun currentScreen(@IdRes itemId: Int) {
        val toolbarTitle = when (itemId) {
            R.id.menu_jogos -> "Jogos"
            R.id.menu_calendario -> "Calendário"
            R.id.menu_feed -> "Feed de Notícias"
            R.id.menu_listas -> "Listas"
            R.id.menu_estatisticas -> "Estatísticas"
            else -> "GamerGuide"
        }

        val tabLayoutVisibility = if (itemId == R.id.menu_jogos) View.VISIBLE else View.GONE
        tabLayout.visibility = tabLayoutVisibility

        toolbar.toolbar_title.text = toolbarTitle
    }
}
