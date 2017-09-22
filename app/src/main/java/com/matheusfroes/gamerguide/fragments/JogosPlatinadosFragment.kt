package com.matheusfroes.gamerguide.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.matheusfroes.gamerguide.R

/**
 * Created by matheus_froes on 21/09/2017.
 */
class JogosPlatinadosFragment : Fragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_jogos_platinados, container, false)
    }
}