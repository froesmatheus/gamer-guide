package com.matheusfroes.gamerguide.ui.feed

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.net.Uri
import android.os.Bundle
import android.support.customtabs.CustomTabsIntent
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.view.*
import com.matheusfroes.gamerguide.*
import com.matheusfroes.gamerguide.data.model.News
import com.matheusfroes.gamerguide.extra.Result
import com.matheusfroes.gamerguide.extra.appCompatActivity
import com.matheusfroes.gamerguide.extra.appInjector
import com.matheusfroes.gamerguide.extra.viewModelProvider
import com.matheusfroes.gamerguide.widget.VerticalSpaceItemDecoration
import kotlinx.android.synthetic.main.activity_feed.*
import kotlinx.android.synthetic.main.toolbar.*
import kotlinx.android.synthetic.main.toolbar.view.*
import timber.log.Timber
import javax.inject.Inject


class FeedFragment : Fragment() {
    val adapter: FeedAdapter by lazy { FeedAdapter() }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: FeedViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        return inflater.inflate(R.layout.activity_feed, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        appInjector.inject(this)
        setupToolbar()

        viewModel = viewModelProvider(viewModelFactory)

        viewModel.feedState.observe(this, Observer { result ->
            when (result) {
                is Result.Complete -> {
                    adapter.news = result.data
                    hideLoadingIndicator()
                }
                is Result.InProgress -> {
                    if (result.cachedData != null && result.cachedData.isNotEmpty()) {
                        adapter.news = result.cachedData
                    } else {
                        showLoadingIndicator()
                    }
                }
                is Result.Error -> {
                    hideLoadingIndicator()
                    Timber.e(result.error)
                }
            }
        })

        viewModel.fetchNews()

        rvNoticias.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        rvNoticias.addItemDecoration(VerticalSpaceItemDecoration(50))

        rvNoticias.adapter = adapter

        adapter.setOnClickListener(object : FeedAdapter.OnNewsClickListener {
            override fun onClick(news: News) {
                val customTabsIntent = CustomTabsIntent.Builder()
                        .setToolbarColor(ContextCompat.getColor(requireActivity(), R.color.colorPrimary))
                        .build()
                customTabsIntent.launchUrl(activity, Uri.parse(news.url))
            }
        })

        swipeRefreshLayout.setOnRefreshListener {
            viewModel.fetchNews()
        }
    }

    private fun setupToolbar() {
        appCompatActivity.setSupportActionBar(toolbar)
        appCompatActivity.supportActionBar?.setDisplayShowTitleEnabled(false)
        toolbar.toolbarTitle.text = getString(R.string.feed_noticias)
    }

    private fun showLoadingIndicator() {
        swipeRefreshLayout.isRefreshing = true
    }

    private fun hideLoadingIndicator() {
        swipeRefreshLayout.isRefreshing = false
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_feed, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.navAtualizarFeed -> {
                viewModel.fetchNews()
                return true
            }
        }
        return false
    }
}