package com.matheusfroes.gamerguide.ui.gamedetail

import android.content.Context
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import com.matheusfroes.gamerguide.R

class DetalhesJogosFragmentAdapter(val context: Context, fm: FragmentManager?) : FragmentStatePagerAdapter(fm) {
    private val tabItems = mutableListOf(
            InformacoesGeraisJogoFragment(), VideosFragment(), StreamsFragment()
    )
    private val tabTitles = mutableListOf(
            context.getString(R.string.tab_informacoes),
            context.getString(R.string.tab_videos),
            context.getString(R.string.streams)
    )

    override fun getItem(position: Int) = tabItems[position]

    override fun getCount() = tabItems.size

    override fun getPageTitle(position: Int): String = tabTitles[position]

    fun removeTabPage(position: Int) {
        if (!tabItems.isEmpty() && position < tabItems.size) {
            tabItems.removeAt(position)
            tabTitles.removeAt(position)
            notifyDataSetChanged()
        }
    }
}