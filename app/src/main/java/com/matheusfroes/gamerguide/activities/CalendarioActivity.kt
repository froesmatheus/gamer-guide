package com.matheusfroes.gamerguide.activities

import android.os.Bundle
import com.matheusfroes.gamerguide.R
import kotlinx.android.synthetic.main.toolbar.*

/**
 * Created by matheusfroes on 04/10/2017.
 */
class CalendarioActivity : BaseActivityDrawer() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendario)
        setSupportActionBar(toolbar)
        configurarDrawer()
        title = "Calendário"
    }


    override fun onStart() {
        super.onStart()

        setDrawerSelectedItem(BaseActivityDrawer.CALENDARIO_IDENTIFIER)
    }
}