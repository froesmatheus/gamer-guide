package com.matheusfroes.gamer_guide.adapters

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.matheusfroes.gamer_guide.fragments.JogosNaoZeradosFragment
import com.matheusfroes.gamer_guide.fragments.JogosZeradosFragment

/**
 * Created by matheusfroes on 20/09/2017.
 */
class MeusJogosAdapter(fragmentManager: FragmentManager) : FragmentPagerAdapter(fragmentManager) {
    private val jogosZerados = JogosZeradosFragment()
    private val jogosNaoZerados = JogosNaoZeradosFragment()

    override fun getItem(position: Int): Fragment = when (position) {
        0 -> jogosZerados
        1 -> jogosNaoZerados
        2 -> jogosNaoZerados
        else -> jogosZerados
    }

    override fun getCount() = 3

    override fun getPageTitle(position: Int): String = when (position) {
        0 -> "Não Terminados"
        1 -> "Zerados"
        2 -> "Platinados"
        else -> "Zerados"
    }
}