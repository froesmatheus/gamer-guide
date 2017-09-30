package com.matheusfroes.gamerguide.adapters

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import com.matheusfroes.gamerguide.fragments.JogosTabFragment

/**
 * Created by matheusfroes on 20/09/2017.
 */
class JogosFragmentAdapter(fragmentManager: FragmentManager) : FragmentStatePagerAdapter(fragmentManager) {
    private val jogosZerados = JogosTabFragment()
    private val jogosNaoTerminados = JogosTabFragment()

    override fun getItem(position: Int): Fragment {
        val bundle = Bundle()
        return when (position) {
            0 -> {
                bundle.putString("tipo_jogo", "nao_terminado")
                jogosNaoTerminados.arguments = bundle
                jogosNaoTerminados
            }
            1 -> {
                bundle.putString("tipo_jogo", "zerados")
                jogosZerados.arguments = bundle
                jogosZerados
            }
            else -> jogosZerados
        }
    }

    override fun getCount() = 2

    override fun getPageTitle(position: Int): String = when (position) {
        0 -> "Não Terminados"
        1 -> "Zerados"
        else -> "Zerados"
    }
}