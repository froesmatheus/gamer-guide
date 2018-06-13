package com.matheusfroes.gamerguide.ui.detalhesjogo

import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.matheusfroes.gamerguide.EndlessScrollListener
import com.matheusfroes.gamerguide.R
import com.matheusfroes.gamerguide.activity
import com.matheusfroes.gamerguide.network.ApiService
import com.matheusfroes.gamerguide.network.data.Stream
import kotlinx.android.synthetic.main.fragment_streams.*
import kotlinx.android.synthetic.main.fragment_streams.view.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class StreamsFragment : Fragment() {
    private val viewModel: DetalhesJogoViewModel by lazy {
        ViewModelProviders.of(activity()).get(DetalhesJogoViewModel::class.java)
    }
    private val adapter: StreamsAdapter by lazy {
        StreamsAdapter(activity())
    }
    val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(ApiService.URL_BASE)
            .build()

    val service = retrofit.create(ApiService::class.java)
    var apiOffset = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val theme = if (viewModel.temaAtual.value == "DEFAULT") R.style.AppTheme_NoActionBar else R.style.AppTheme_OLED

        val context = ContextThemeWrapper(activity, theme)
        val localInflater = inflater.cloneInContext(context)
        val view = localInflater.inflate(R.layout.fragment_streams, container, false)


        val layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        view.rvStreams.emptyView = layoutEmpty
        view.rvStreams.layoutManager = layoutManager
        view.rvStreams.adapter = adapter

        val nomeJogo = viewModel.jogo.value?.name!!

        obterStreams(nomeJogo)

        view.rvStreams.addOnScrollListener(object : EndlessScrollListener(layoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                apiOffset += 10
                obterStreams(nomeJogo)
            }
        })


        adapter.setOnStreamClickListener(object : StreamsAdapter.OnStreamClickListener {
            override fun onStreamClick(stream: Stream) {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(stream.channel.url))
                startActivity(intent)
            }
        })

        return view
    }

    fun obterStreams(nomeJogo: String) {
        val call = service.obterStreamsPorJogo(nomeJogo, offset = apiOffset)

        doAsync {
            val response = call.execute()

            if (response.isSuccessful) {
                uiThread {
                    adapter.preencherLista(response.body()?.streams ?: listOf())
                }
            }
        }
    }
}