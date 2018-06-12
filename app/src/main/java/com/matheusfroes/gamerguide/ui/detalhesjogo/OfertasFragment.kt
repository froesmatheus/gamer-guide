package com.matheusfroes.gamerguide.ui.detalhesjogo

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.matheusfroes.gamerguide.R
import com.matheusfroes.gamerguide.activity
import com.matheusfroes.gamerguide.network.ApiService
import kotlinx.android.synthetic.main.fragment_ofertas.*
import kotlinx.android.synthetic.main.fragment_ofertas.view.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class OfertasFragment : Fragment() {
    private val viewModel: DetalhesJogoViewModel by lazy {
        ViewModelProviders.of(activity()).get(DetalhesJogoViewModel::class.java)
    }
    private val adapter: OfertasAdapter by lazy {
        OfertasAdapter(activity())
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
        val view = localInflater.inflate(R.layout.fragment_ofertas, container, false)


        val layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        view.rvOfertas.emptyView = layoutEmpty
        view.rvOfertas.layoutManager = layoutManager
        view.rvOfertas.adapter = adapter

        val nomeJogo = viewModel.jogo.value?.name!!

        obterOfertas(nomeJogo)

//        view.rvOfertas.addOnScrollListener(object : EndlessScrollListener(layoutManager) {
//            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
//                apiOffset += 10
//                obterOfertas(nomeJogo)
//            }
//        })


//        adapter.setOnStreamClickListener(object : StreamsAdapter.OnStreamClickListener {
//            override fun onStreamClick(stream: Stream) {
//                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(stream.channel.url))
//                startActivity(intent)
//            }
//        })

        return view
    }

    fun obterOfertas(nomeJogo: String) {
        val call = service.obterOfertasJogo(nomeJogo)

        doAsync {
            val response = call.execute()

            if (response.isSuccessful) {
                uiThread {
                    adapter.preencherLista(response.body()?.products ?: listOf())
                }
            }
        }
    }
}