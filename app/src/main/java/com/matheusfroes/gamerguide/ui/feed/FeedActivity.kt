package com.matheusfroes.gamerguide.ui.feed

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.net.Uri
import android.os.Bundle
import android.support.customtabs.CustomTabsIntent
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.view.*
import com.matheusfroes.gamerguide.R
import com.matheusfroes.gamerguide.data.models.Noticia
import com.matheusfroes.gamerguide.extra.VerticalSpaceItemDecoration
import com.matheusfroes.gamerguide.ui.TelaPrincipalViewModel
import kotlinx.android.synthetic.main.activity_feed.*
import kotlinx.android.synthetic.main.toolbar.*


class FeedActivity : Fragment() {
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

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.tabLayout?.visibility = View.GONE

        adapter = FeedAdapter(activity)

        viewModel.noticias.observe(this, Observer { noticias ->
            adapter.preencherNoticias(noticias!!)
        })

        viewModel.atualizarFeed()

        rvNoticias.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        rvNoticias.addItemDecoration(VerticalSpaceItemDecoration(50))

        rvNoticias.adapter = adapter

        adapter.setOnClickListener(object : FeedAdapter.OnNewsClickListener {
            override fun onClick(noticia: Noticia) {
                val customTabsIntent = CustomTabsIntent.Builder()
                        .setToolbarColor(ContextCompat.getColor(activity, R.color.colorPrimary))
                        .build()
                customTabsIntent.launchUrl(activity, Uri.parse(noticia.url))
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
}