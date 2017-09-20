package com.matheusfroes.gamer_guide

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.toolbar.*

/**
 * Created by matheus_froes on 19/09/2017.
 */
class FeedFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_feed, container, false)

        activity.tabLayout.visibility = View.GONE

        return view
    }
}