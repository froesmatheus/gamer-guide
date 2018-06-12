package com.matheusfroes.gamerguide.ui.detalhesjogo

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.matheusfroes.gamerguide.R
import com.matheusfroes.gamerguide.activity
import com.matheusfroes.gamerguide.data.model.Video
import kotlinx.android.synthetic.main.fragment_videos.view.*

class VideosFragment : Fragment() {
    private val viewModel: DetalhesJogoViewModel by lazy {
        ViewModelProviders.of(activity()).get(DetalhesJogoViewModel::class.java)
    }
    private val adapter: VideosAdapter by lazy {
        VideosAdapter(activity())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val theme = if (viewModel.temaAtual.value == "DEFAULT") R.style.AppTheme_NoActionBar else R.style.AppTheme_OLED

        val context = ContextThemeWrapper(activity, theme)
        val localInflater = inflater.cloneInContext(context)
        val view = localInflater.inflate(R.layout.fragment_videos, container, false)


        view.rvVideos.layoutManager = GridLayoutManager(activity, 2, GridLayoutManager.VERTICAL, false)
        view.rvVideos.adapter = adapter

        viewModel.jogo.observe(this, Observer { jogo ->
            adapter.preencherLista(jogo?.videos ?: listOf())
        })

        adapter.setOnVideoClickListener(object : VideosAdapter.OnVideoClickListener {
            override fun onVideoClick(video: Video) {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.youtube_url, video.videoId)))
                startActivity(intent)
            }
        })

        return view
    }
}