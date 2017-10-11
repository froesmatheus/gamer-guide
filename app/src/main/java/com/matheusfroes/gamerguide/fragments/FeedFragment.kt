package com.matheusfroes.gamerguide.fragments

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.customtabs.CustomTabsIntent
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.view.*
import com.matheusfroes.gamerguide.R
import com.matheusfroes.gamerguide.activities.ConfiguracoesFeedActivity
import com.matheusfroes.gamerguide.activities.TelaPrincipalViewModel
import com.matheusfroes.gamerguide.adapters.FeedAdapter
import com.matheusfroes.gamerguide.extra.VerticalSpaceItemDecoration
import com.matheusfroes.gamerguide.models.Noticia
import kotlinx.android.synthetic.main.fragment_feed.view.*
import kotlinx.android.synthetic.main.toolbar.*


/**
 * Created by matheus_froes on 19/09/2017.
 */
class FeedFragment : Fragment() {
    private var adapter: FeedAdapter? = null
    var swipeRefreshLayout: SwipeRefreshLayout? = null
    private val viewModel: TelaPrincipalViewModel by lazy {
        ViewModelProviders.of(this).get(TelaPrincipalViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_feed, container, false)
        activity.tabLayout.visibility = View.GONE

        adapter = FeedAdapter(activity)

        viewModel.noticias.observe(this, Observer {
            adapter?.preencherNoticias(viewModel.noticias.value!!)
        })

        viewModel.atualizarFeed()

        view.rvNoticias.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        view.rvNoticias.addItemDecoration(VerticalSpaceItemDecoration(50))

        view.rvNoticias.adapter = adapter

        adapter?.setOnClickListener(object : FeedAdapter.OnNewsClickListener {
            override fun onClick(noticia: Noticia) {
                val customTabsIntent = CustomTabsIntent.Builder()
                        .setToolbarColor(ContextCompat.getColor(activity, R.color.colorPrimary))
                        .build()
                customTabsIntent.launchUrl(activity, Uri.parse(noticia.url))
            }
        })

        swipeRefreshLayout = view.swipeRefreshLayout

        view.swipeRefreshLayout.setOnRefreshListener {
            viewModel.atualizarFeed()
        }
        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_feed, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.navConfiguracaoFeed -> {
                startActivity(Intent(context, ConfiguracoesFeedActivity::class.java))
                return true
            }
            R.id.navAtualizarFeed -> {
                viewModel.atualizarFeed()
                return true
            }
        }
        return false
    }
}