package com.matheusfroes.gamerguide.activities

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.customtabs.CustomTabsIntent
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.matheusfroes.gamerguide.BaseActivity
import com.matheusfroes.gamerguide.R
import com.matheusfroes.gamerguide.adapters.FeedAdapter
import com.matheusfroes.gamerguide.extra.VerticalSpaceItemDecoration
import com.matheusfroes.gamerguide.models.Noticia
import kotlinx.android.synthetic.main.fragment_feed.*
import kotlinx.android.synthetic.main.toolbar.*


/**
 * Created by matheus_froes on 19/09/2017.
 */
class FeedActivity : BaseActivity() {
    lateinit var adapter: FeedAdapter
    private val viewModel: TelaPrincipalViewModel by lazy {
        ViewModelProviders.of(this).get(TelaPrincipalViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_feed)
        setSupportActionBar(toolbar)
        configurarDrawer()

        tabLayout.visibility = View.GONE

        title = "Feed de notícias"

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
                customTabsIntent.launchUrl(applicationContext, Uri.parse(noticia.url))
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

        setDrawerSelectedItem(BaseActivity.FEED_IDENTIFIER)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.navConfiguracaoFeed -> {
                startActivity(Intent(this, ConfiguracoesFeedActivity::class.java))
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