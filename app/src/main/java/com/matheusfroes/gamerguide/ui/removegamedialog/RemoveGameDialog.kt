package com.matheusfroes.gamerguide.ui.removegamedialog

import android.arch.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.matheusfroes.gamerguide.R
import com.matheusfroes.gamerguide.extra.appInjector
import com.matheusfroes.gamerguide.data.model.InsertType
import com.matheusfroes.gamerguide.extra.viewModelProvider
import com.matheusfroes.gamerguide.widget.CustomDialogFragment
import kotlinx.android.synthetic.main.removegame_dialog_view.*
import javax.inject.Inject


class RemoveGameDialog : CustomDialogFragment() {
    companion object {
        private const val GAME_ID_PARAMETER = "gameId"

        fun newInstance(gameId: Long) = RemoveGameDialog().apply {
            arguments = Bundle().apply {
                putSerializable(GAME_ID_PARAMETER, gameId)
            }
        }
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: RemoveGameViewModel

    var gameRemovedEvent: ((Boolean) -> Unit)? = null

    private var gameIsInGameLists: Boolean = false
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
            inflater.inflate(R.layout.removegame_dialog_view, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        appInjector.inject(this)
        viewModel = viewModelProvider(viewModelFactory)

        val gameId = arguments?.getLong(GAME_ID_PARAMETER) ?: -1

        btnCancel.setOnClickListener { dismiss() }

        btnRemoveGame.setOnClickListener { removeGame(gameId) }

        gameIsInGameLists = viewModel.gameIsInGameLists(gameId)

        if (!gameIsInGameLists) {
            chkRemoverDasListas.visibility = View.GONE
        }
    }

    private fun removeGame(gameId: Long) {
        val removeFromLists = chkRemoverDasListas.isChecked

        val game = viewModel.getGameByInsertType(gameId)

        try {
            if (removeFromLists || !gameIsInGameLists) {
                viewModel.removeGameFromLists(gameId)
                viewModel.removeGame(gameId)
            } else {
                game?.insertType = InsertType.INSERT_TO_LIST
                game?.let { viewModel.updateGame(it) }
            }
            gameRemovedEvent?.invoke(true)
        } catch (e: Exception) {
            gameRemovedEvent?.invoke(false)
        }

        dismiss()
    }
}