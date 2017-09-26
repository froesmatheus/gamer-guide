package com.matheusfroes.gamerguide.adapters

import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import com.matheusfroes.gamerguide.fragments.InformacoesGeraisJogoFragment
import com.matheusfroes.gamerguide.fragments.MinhaJogatinaFragment

/**
 * Created by matheus_froes on 26/09/2017.
 */
class DetalhesJogosFragmentAdapter(fm: FragmentManager?) : FragmentStatePagerAdapter(fm) {
    private val informacoesGerais = InformacoesGeraisJogoFragment()
    private val minhaJogatina = MinhaJogatinaFragment()

    override fun getItem(position: Int) = when (position) {
        0 -> informacoesGerais
        1 -> minhaJogatina
        else -> informacoesGerais
    }

    override fun getCount() = 2

    override fun getPageTitle(position: Int) = when (position) {
        0 -> "Informações Gerais"
        1 -> "Minha Jogatina"
        else -> ""
    }
}