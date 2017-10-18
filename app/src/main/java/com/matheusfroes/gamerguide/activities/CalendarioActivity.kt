package com.matheusfroes.gamerguide.activities

import android.os.Bundle
import android.view.View
import com.matheusfroes.gamerguide.R
import kotlinx.android.synthetic.main.toolbar.*

/**
 * Created by matheusfroes on 04/10/2017.
 */
class CalendarioActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_calendario)
        setSupportActionBar(toolbar)
        configurarDrawer()

        tabLayout.visibility = View.GONE
        title = "Calendário"
    }


    override fun onStart() {
        super.onStart()

        setDrawerSelectedItem(BaseActivity.CALENDARIO_IDENTIFIER)
    }
}