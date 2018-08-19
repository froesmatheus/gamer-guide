package com.matheusfroes.gamerguide.ui.gamedetails.livestream

import android.arch.lifecycle.ViewModelProvider
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
import com.matheusfroes.gamerguide.*
import com.matheusfroes.gamerguide.ui.gamedetails.GameDetailsViewModel
import kotlinx.android.synthetic.main.fragment_streams.*
import timber.log.Timber
import javax.inject.Inject

class StreamsFragment : Fragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject
    lateinit var userPreferences: UserPreferences
    private lateinit var viewModel: GameDetailsViewModel

    private val adapter: StreamsAdapter by lazy { StreamsAdapter() }
    var apiOffset = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        appInjector.inject(this)
        viewModel = activityViewModelProvider(viewModelFactory)

        val theme = userPreferences.getCurrentAppTheme()

        val context = ContextThemeWrapper(activity, theme)
        val localInflater = inflater.cloneInContext(context)

        return localInflater.inflate(R.layout.fragment_streams, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        rvStreams.emptyView = layoutEmpty
        rvStreams.layoutManager = layoutManager
        rvStreams.adapter = adapter

        getLivestreamsByGame()

        rvStreams.addOnScrollListener(object : EndlessScrollListener(layoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                apiOffset += 10
                getLivestreamsByGame()
            }
        })

        adapter.streamClick = { stream ->
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(stream.channel.url))
            startActivity(intent)
        }
    }

    fun getLivestreamsByGame() {
        viewModel.getLivestreamsByGame(offset = apiOffset).subscribe({ streams ->
            adapter.streams = streams.toMutableList()
        }) {
            Timber.e(it)
        }
    }
}