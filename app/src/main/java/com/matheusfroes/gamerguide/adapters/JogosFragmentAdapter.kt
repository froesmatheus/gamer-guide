package com.matheusfroes.gamerguide.adapters

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import com.matheusfroes.gamerguide.fragments.JogosNaoTerminadosFragment
import com.matheusfroes.gamerguide.fragments.JogosZeradosFragment

/**
 * Created by matheusfroes on 20/09/2017.
 */
class JogosFragmentAdapter(fragmentManager: FragmentManager) : FragmentStatePagerAdapter(fragmentManager) {
    private val jogosZerados = JogosZeradosFragment()
    private val jogosNaoTerminados = JogosNaoTerminadosFragment()

    override fun getItem(position: Int): Fragment = when (position) {
        0 -> jogosNaoTerminados
        1 -> jogosZerados
        else -> jogosZerados
    }

    override fun getCount() = 2

    override fun getPageTitle(position: Int): String = when (position) {
        0 -> "Não Terminados"
        1 -> "Zerados"
        else -> "Zerados"
    }
}