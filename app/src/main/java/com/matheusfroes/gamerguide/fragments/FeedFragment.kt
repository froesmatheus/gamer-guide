package com.matheusfroes.gamerguide.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.customtabs.CustomTabsIntent
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.view.*
import com.matheusfroes.gamerguide.R
import com.matheusfroes.gamerguide.VerticalSpaceItemDecoration
import com.matheusfroes.gamerguide.activities.ConfiguracoesFeed
import com.matheusfroes.gamerguide.adapters.NoticiasAdapter
import com.matheusfroes.gamerguide.models.Noticia
import com.pkmmte.pkrss.Article
import com.pkmmte.pkrss.Callback
import com.pkmmte.pkrss.PkRSS
import kotlinx.android.synthetic.main.fragment_feed.*
import kotlinx.android.synthetic.main.fragment_feed.view.*
import kotlinx.android.synthetic.main.toolbar.*


/**
 * Created by matheus_froes on 19/09/2017.
 */
class FeedFragment : Fragment(), Callback {
    private var adapter: NoticiasAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_feed, container, false)
        activity.tabLayout.visibility = View.GONE

        adapter = NoticiasAdapter(activity)

        PkRSS.with(activity).load("http://rss.baixakijogos.com.br/feed/").callback(this).async()

        view.rvNoticias.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        view.rvNoticias.addItemDecoration(VerticalSpaceItemDecoration(50))

        view.rvNoticias.adapter = adapter

        adapter?.setOnClickListener(object : NoticiasAdapter.OnNewsClickListener {
            override fun onClick(noticia: Noticia) {
                val customTabsIntent = CustomTabsIntent.Builder()
                        .setToolbarColor(ContextCompat.getColor(activity, R.color.colorPrimary))
                        .build()
                customTabsIntent.launchUrl(activity, Uri.parse(noticia.url))
            }
        })

        view.swipeRefreshLayout.setOnRefreshListener {
            PkRSS.with(activity).load("http://rss.baixakijogos.com.br/feed/").callback(this).async()
        }
        return view
    }

    override fun onLoadFailed() {
    }

    override fun onPreload() {
        swipeRefreshLayout?.isRefreshing = true
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_feed, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.navConfiguracaoFeed -> {
                startActivity(Intent(context, ConfiguracoesFeed::class.java))
                return true
            }
        }
        return false
    }

    override fun onLoaded(newArticles: MutableList<Article>) {
        val noticias = newArticles.map { article ->
            val imagemNoticia: String? = if (article.enclosure == null) {
                null
            } else {
                article.enclosure.url
            }

            Noticia(article.title, imagemNoticia!!, article.source.toString(), article.date)
        }
        adapter?.preencherNoticias(noticias)
        swipeRefreshLayout?.isRefreshing = false
    }

}