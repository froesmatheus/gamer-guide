package com.matheusfroes.gamerguide.adapters

import android.content.Context
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import com.matheusfroes.gamerguide.R
import com.matheusfroes.gamerguide.fragments.InformacoesGeraisJogoFragment
import com.matheusfroes.gamerguide.fragments.MeuProgressoFragment
import com.matheusfroes.gamerguide.fragments.VideosFragment

/**
 * Created by matheus_froes on 26/09/2017.
 */
class DetalhesJogosFragmentAdapter(val context: Context, fm: FragmentManager?) : FragmentStatePagerAdapter(fm) {
    private val tabItems = mutableListOf(
            MeuProgressoFragment(), InformacoesGeraisJogoFragment(), VideosFragment()
    )
    private val tabTitles = mutableListOf(
            context.getString(R.string.tab_progresso),
            context.getString(R.string.tab_informacoes),
            context.getString(R.string.tab_videos)
    )

    override fun getItem(position: Int) = tabItems[position]

    override fun getCount() = tabItems.size

    override fun getPageTitle(position: Int) = tabTitles[position]

    fun removeTabPage(position: Int) {
        if (!tabItems.isEmpty() && position < tabItems.size) {
            tabItems.removeAt(position)
            tabTitles.removeAt(position)
            notifyDataSetChanged()
        }
    }

    fun addTabPage(position: Int, fragment: Fragment, title: String) {
        tabItems.add(position, fragment)
        tabTitles.add(position, title)
        notifyDataSetChanged()
    }
}