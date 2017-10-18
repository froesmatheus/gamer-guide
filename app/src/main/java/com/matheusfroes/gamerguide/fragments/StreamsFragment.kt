package com.matheusfroes.gamerguide.fragments

import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.matheusfroes.gamerguide.R
import com.matheusfroes.gamerguide.activities.DetalhesJogoViewModel
import com.matheusfroes.gamerguide.adapters.StreamsAdapter
import com.matheusfroes.gamerguide.api.ApiService
import com.matheusfroes.gamerguide.models.Stream
import kotlinx.android.synthetic.main.fragment_streams.view.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by matheusfroes on 30/09/2017.
 */
class StreamsFragment : Fragment() {
    private val viewModel: DetalhesJogoViewModel by lazy {
        ViewModelProviders.of(activity).get(DetalhesJogoViewModel::class.java)
    }
    private val adapter: StreamsAdapter by lazy {
        StreamsAdapter(activity)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.fragment_streams, container, false)

        view.rvVideos.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        view.rvVideos.adapter = adapter


        val nomeJogo = viewModel.jogo.value?.nome!!

        val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(ApiService.URL_BASE)
                .build()

        val service = retrofit.create(ApiService::class.java)

        val call = service.obterStreamsPorJogo(nomeJogo)

        doAsync {
            val response = call.execute()

            if (response.isSuccessful) {
                uiThread {
                    adapter.preencherLista(response.body()?.streams ?: listOf())
                }
            }
        }

        adapter.setOnStreamClickListener(object : StreamsAdapter.OnStreamClickListener {
            override fun onStreamClick(stream: Stream) {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(stream.channel.url))
                startActivity(intent)
            }
        })

        return view
    }
}