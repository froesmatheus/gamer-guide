package com.matheusfroes.gamer_guide.activities

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import com.matheusfroes.gamer_guide.fragments.FeedFragment
import com.matheusfroes.gamer_guide.fragments.ListasFragment
import com.matheusfroes.gamer_guide.fragments.MeusJogosFragment
import com.matheusfroes.gamer_guide.R
import kotlinx.android.synthetic.main.activity_tela_principal.*
import kotlinx.android.synthetic.main.toolbar.*

class TelaPrincipalActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tela_principal)
        setSupportActionBar(toolbar)

        navigation.setOnNavigationItemSelectedListener(this)

        navigation.selectedItemId = R.id.navigation_home
    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.navigation_home -> {
                mudarTela(0)
                return true
            }
            R.id.navigation_dashboard -> {
                mudarTela(1)
                return true
            }
            R.id.navigation_notifications -> {
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
