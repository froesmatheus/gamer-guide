package com.matheusfroes.gamerguide.ui.addgamedialog

import android.arch.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.matheusfroes.gamerguide.R
import com.matheusfroes.gamerguide.extra.appInjector
import com.matheusfroes.gamerguide.data.model.Game
import com.matheusfroes.gamerguide.data.model.GameList
import com.matheusfroes.gamerguide.data.model.InsertType
import com.matheusfroes.gamerguide.extra.viewModelProvider
import com.matheusfroes.gamerguide.widget.CustomDialogFragment
import kotlinx.android.synthetic.main.addgame_dialog_view.*
import timber.log.Timber
import javax.inject.Inject


class AddGameDialog : CustomDialogFragment() {
    companion object {
        private const val GAME_PARAMETER = "game"

        fun newInstance(game: Game) = AddGameDialog().apply {
            arguments = Bundle().apply {
                putSerializable(GAME_PARAMETER, game)
            }
        }
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: AddGameViewModel

    var gameAddedEvent: ((Boolean) -> Unit)? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
            inflater.inflate(R.layout.addgame_dialog_view, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        appInjector.inject(this)

        val game = arguments?.getSerializable(GAME_PARAMETER) as Game

        viewModel = viewModelProvider(viewModelFactory)

        radioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.rbUnfinishedGames -> spGameLists.visibility = View.GONE
                R.id.rbBeatenGames -> spGameLists.visibility = View.GONE
                R.id.rbGameList -> {
                    spGameLists.visibility = View.VISIBLE
                    fetchGameLists()
                }
            }
        }

        btnCancel.setOnClickListener { dismiss() }

        btnSaveGame.setOnClickListener { saveGame(game) }
    }

    private fun saveGame(game: Game) {
        when (radioGroup.checkedRadioButtonId) {
            R.id.rbUnfinishedGames -> game.progress.beaten = false
            R.id.rbBeatenGames -> game.progress.beaten = true
        }

        try {
            // If "List" if the option selected, add game to list
            if (radioGroup.checkedRadioButtonId == R.id.rbGameList) {
                val selectedList = spGameLists.selectedItem as GameList
                game.insertType = InsertType.INSERT_TO_LIST
                viewModel.addGame(game)
                viewModel.addGameToList(game.id, selectedList.id)
            } else {
                viewModel.addGame(game)
            }
            gameAddedEvent?.invoke(true)
        } catch (e: Exception) {
            Timber.e(e)
            gameAddedEvent?.invoke(false)
        }
        dismiss()
    }

    private fun fetchGameLists() {
        val gameLists = viewModel.getGameLists()
        val spinnerAdapter = ArrayAdapter<GameList>(activity, android.R.layout.simple_spinner_dropdown_item, gameLists)

        spGameLists.adapter = spinnerAdapter
    }
}