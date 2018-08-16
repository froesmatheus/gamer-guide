package com.matheusfroes.gamerguide.ui.calendar

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.matheusfroes.gamerguide.EndlessScrollListener
import com.matheusfroes.gamerguide.R
import com.matheusfroes.gamerguide.appInjector
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import kotlinx.android.synthetic.main.activity_calendario.*
import kotlinx.android.synthetic.main.toolbar.*
import javax.inject.Inject

class CalendarFragment : Fragment() {
    private val lancamentosAdapter: LancamentosAdapter by lazy { LancamentosAdapter() }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: CalendarViewModel

    private val subscriptions = CompositeDisposable()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
            inflater.inflate(R.layout.activity_calendario, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        appInjector.inject(this)

        viewModel = ViewModelProviders.of(this, viewModelFactory)[CalendarViewModel::class.java]

        activity?.tabLayout?.visibility = View.GONE


        val layoutManager = LinearLayoutManager(activity)
        rvLancamentos.layoutManager = layoutManager
        rvLancamentos.adapter = lancamentosAdapter
        rvLancamentos.addOnScrollListener(object : EndlessScrollListener(layoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                obterLancamentos()
            }
        })

        obterLancamentos()
    }

    private fun obterLancamentos() {
        subscriptions += viewModel.getGameReleases().subscribe { releases ->
            lancamentosAdapter.preencherLista(releases.distinctBy { it.game.name }.toMutableList())
        }
    }
}