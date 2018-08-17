package com.matheusfroes.gamerguide.ui.gamedetails.video

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
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
import com.matheusfroes.gamerguide.UserPreferences
import com.matheusfroes.gamerguide.appInjector
import com.matheusfroes.gamerguide.ui.gamedetails.GameDetailsViewModel
import kotlinx.android.synthetic.main.fragment_videos.view.*
import javax.inject.Inject

class VideosFragment : Fragment() {
    private val adapter: VideosAdapter by lazy { VideosAdapter() }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var userPreferences: UserPreferences
    private lateinit var viewModel: GameDetailsViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        appInjector.inject(this)
        viewModel = ViewModelProviders.of(requireActivity(), viewModelFactory)[GameDetailsViewModel::class.java]

        val theme = userPreferences.getCurrentAppTheme()

        val context = ContextThemeWrapper(activity, theme)
        val localInflater = inflater.cloneInContext(context)
        val view = localInflater.inflate(R.layout.fragment_videos, container, false)

        view.rvVideos.layoutManager = GridLayoutManager(activity, 2, GridLayoutManager.VERTICAL, false)
        view.rvVideos.adapter = adapter

        viewModel.game.observe(this, Observer { game ->
            if (game != null) adapter.videos = game.videos
        })

        adapter.videoClick = { video ->
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.youtube_url, video.videoId)))
            startActivity(intent)
        }

        return view
    }
}