package com.matheusfroes.gamerguide.ui.feed

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.net.Uri
import android.os.Bundle
import android.support.customtabs.CustomTabsIntent
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.view.*
import com.matheusfroes.gamerguide.*
import com.matheusfroes.gamerguide.data.model.News
import com.matheusfroes.gamerguide.extra.VerticalSpaceItemDecoration
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.activity_feed.*
import javax.inject.Inject


class FeedFragment : DaggerFragment() {
    lateinit var adapter: FeedAdapter
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var viewModel: FeedViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
            inflater.inflate(R.layout.activity_feed, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        adapter = FeedAdapter(context())

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(FeedViewModel::class.java)


        viewModel.news.observe(this, Observer { news ->
            adapter.news = news.orEmpty()
        })

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
            viewModel.refreshFeed()
            swipeRefreshLayout.isRefreshing = false
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_feed, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.navAtualizarFeed -> {
                viewModel.refreshFeed()
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