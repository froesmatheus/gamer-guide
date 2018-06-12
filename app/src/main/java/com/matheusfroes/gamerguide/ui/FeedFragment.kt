package com.matheusfroes.gamerguide.ui

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.net.Uri
import android.os.Bundle
import android.support.customtabs.CustomTabsIntent
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.view.*
import com.matheusfroes.gamerguide.MainActivity
import com.matheusfroes.gamerguide.R
import com.matheusfroes.gamerguide.activity
import com.matheusfroes.gamerguide.context
import com.matheusfroes.gamerguide.data.model.News
import com.matheusfroes.gamerguide.extra.VerticalSpaceItemDecoration
import com.matheusfroes.gamerguide.ui.feed.FeedAdapter


class FeedFragment : Fragment() {
    lateinit var adapter: FeedAdapter
    private val viewModel: TelaPrincipalViewModel by lazy {
        ViewModelProviders.of(this).get(TelaPrincipalViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
            inflater.inflate(R.layout.activity_feed, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        adapter = FeedAdapter(context())

        viewModel.noticias.observe(this, Observer { noticias ->
            adapter.preencherNoticias(noticias!!)
        })

        viewModel.atualizarFeed()

        rvNoticias.layoutManager = LinearLayoutManager(context(), LinearLayoutManager.VERTICAL, false)
        rvNoticias.addItemDecoration(VerticalSpaceItemDecoration(50))

        rvNoticias.adapter = adapter

        adapter.setOnClickListener(object : FeedAdapter.OnNewsClickListener {
            override fun onClick(noticia: News) {
                val customTabsIntent = CustomTabsIntent.Builder()
                        .setToolbarColor(ContextCompat.getColor(context(), R.color.colorPrimary))
                        .build()
                customTabsIntent.launchUrl(context(), Uri.parse(noticia.url))
            }
        })

        swipeRefreshLayout.setOnRefreshListener {
            viewModel.atualizarFeed()
            swipeRefreshLayout.isRefreshing = false
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_feed, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
//            R.id.navConfiguracaoFeed -> {
//                startActivity(Intent(this, ConfiguracoesFeedActivity::class.java))
//                return true
//            }
            R.id.navAtualizarFeed -> {
                viewModel.atualizarFeed()
                return true
            }
        }
        return false
    }

    override fun onResume() {
        super.onResume()

        (activity() as MainActivity).currentScreen(R.id.menu_feed)
    }

}