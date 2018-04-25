package com.matheusfroes.gamerguide.ui.feed

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.net.Uri
import android.os.Bundle
import android.support.customtabs.CustomTabsIntent
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import com.matheusfroes.gamerguide.R
import com.matheusfroes.gamerguide.data.models.Noticia
import com.matheusfroes.gamerguide.extra.VerticalSpaceItemDecoration
import com.matheusfroes.gamerguide.ui.BaseActivityDrawer
import com.matheusfroes.gamerguide.ui.TelaPrincipalViewModel
import kotlinx.android.synthetic.main.activity_feed.*
import kotlinx.android.synthetic.main.toolbar.*


class FeedActivity : BaseActivityDrawer() {
    lateinit var adapter: FeedAdapter
    private val viewModel: TelaPrincipalViewModel by lazy {
        ViewModelProviders.of(this).get(TelaPrincipalViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feed)
        setSupportActionBar(toolbar)
        configurarDrawer()

        title = getString(R.string.feed_noticias)

        adapter = FeedAdapter(this)

        viewModel.noticias.observe(this, Observer { noticias ->
            adapter.preencherNoticias(noticias!!)
        })

        viewModel.atualizarFeed()

        rvNoticias.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rvNoticias.addItemDecoration(VerticalSpaceItemDecoration(50))

        rvNoticias.adapter = adapter

        adapter.setOnClickListener(object : FeedAdapter.OnNewsClickListener {
            override fun onClick(noticia: Noticia) {
                val customTabsIntent = CustomTabsIntent.Builder()
                        .setToolbarColor(ContextCompat.getColor(applicationContext, R.color.colorPrimary))
                        .build()
                customTabsIntent.launchUrl(this@FeedActivity, Uri.parse(noticia.url))
            }
        })

        swipeRefreshLayout.setOnRefreshListener {
            viewModel.atualizarFeed()
            swipeRefreshLayout.isRefreshing = false
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_feed, menu)
        return true
    }

    override fun onStart() {
        super.onStart()

        setDrawerSelectedItem(FEED_IDENTIFIER)
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
}