package com.matheusfroes.gamerguide.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.matheusfroes.gamerguide.R
import kotlinx.android.synthetic.main.toolbar.*

/**
 * Created by matheusfroes on 04/10/2017.
 */
class EstatisticasFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_estatisticas, container, false)
        activity.tabLayout.visibility = View.GONE


        return view
    }
}