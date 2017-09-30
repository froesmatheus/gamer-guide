package com.matheusfroes.gamerguide.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.matheusfroes.gamerguide.R
import com.matheusfroes.gamerguide.adapters.MeusJogosAdapter
import kotlinx.android.synthetic.main.activity_tela_principal.*
import kotlinx.android.synthetic.main.fragment_jogos_zerados.view.*

/**
 * Created by matheusfroes on 20/09/2017.
 */
class JogosZeradosFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_jogos_zerados, container, false)

        val adapter = MeusJogosAdapter(activity)

        view.rvJogosZerados.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        view.rvJogosZerados.emptyView = view.layoutEmpty
        view.rvJogosZerados.adapter = adapter

        view.rvJogosZerados.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                if (dy > 0 && activity.bottomNavigation.isShown) {
                    activity.bottomNavigation.visibility = View.GONE
                } else if (dy < 0) {
                    activity.bottomNavigation.visibility = View.VISIBLE
                }
            }
        })
        return view
    }
}