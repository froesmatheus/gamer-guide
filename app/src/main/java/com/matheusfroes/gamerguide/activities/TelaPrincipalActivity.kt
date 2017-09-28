package com.matheusfroes.gamerguide.activities

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import com.matheusfroes.gamerguide.R
import com.matheusfroes.gamerguide.fragments.FeedFragment
import com.matheusfroes.gamerguide.fragments.ListasFragment
import com.matheusfroes.gamerguide.fragments.MeusJogosFragment
import kotlinx.android.synthetic.main.activity_tela_principal.*
import kotlinx.android.synthetic.main.toolbar.*

class TelaPrincipalActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tela_principal)
        setSupportActionBar(toolbar)

        bottomNavigation.setOnNavigationItemSelectedListener(this)

        bottomNavigation.selectedItemId = R.id.navMeusJogos

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.navMeusJogos -> {
                mudarTela(0)
                return true
            }
            R.id.navFeed -> {
                mudarTela(1)
                return true
            }
            R.id.navListas -> {
                mudarTela(2)
                return true
            }
        }
        return false
    }


    private fun mudarTela(position: Int) {

        val fragment = when (position) {
            0 -> MeusJogosFragment()
            1 -> FeedFragment()
            2 -> ListasFragment()
            else -> MeusJogosFragment()
        }
        supportFragmentManager.beginTransaction().replace(R.id.content, fragment).commit()
    }

}
