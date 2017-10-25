package com.matheusfroes.gamerguide.adapters

import android.content.Context
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import com.matheusfroes.gamerguide.R
import com.matheusfroes.gamerguide.fragments.JogosTabNaoTerminadosFragment
import com.matheusfroes.gamerguide.fragments.JogosTabZeradosFragment


class JogosFragmentAdapter(val context: Context, fragmentManager: FragmentManager) : FragmentStatePagerAdapter(fragmentManager) {
    private val jogosZerados = JogosTabZeradosFragment()
    private val jogosNaoTerminados = JogosTabNaoTerminadosFragment()

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> jogosNaoTerminados
            1 -> jogosZerados
            else -> jogosZerados
        }
    }

    override fun getCount() = 2

    override fun getPageTitle(position: Int): String = when (position) {
        0 -> context.getString(R.string.nao_terminados)
        1 -> context.getString(R.string.zerados)
        else -> context.getString(R.string.zerados)
    }
}