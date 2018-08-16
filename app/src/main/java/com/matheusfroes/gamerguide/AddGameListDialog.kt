package com.matheusfroes.gamerguide

import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import kotlinx.android.synthetic.main.dialog_adicionar_lista.*


class AddGameListDialog : DialogFragment() {

    companion object {
        val ADD_LIST = "add_list"
        val UPDATE_LIST = "update_list"

        fun newInstance(dialogType: String) = AddGameListDialog().apply {
            arguments = Bundle().apply {
                putString("DIALOG_TYPE", dialogType)
            }
        }
    }

    private lateinit var addButtonClicked: ((String) -> Unit)
    private lateinit var listAlreadyAdded: ((String) -> Boolean)

    fun addButtonClick(listener: ((String) -> Unit)) {
        this.addButtonClicked = listener
    }

    fun listAlreadyAdded(listener: ((String) -> Boolean)) {
        this.listAlreadyAdded = listener
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)

        dialog.window.requestFeature(Window.FEATURE_NO_TITLE)
        return dialog
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
            inflater.inflate(R.layout.dialog_adicionar_lista, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val dialogType = arguments?.getString("DIALOG_TYPE")

        btnAdicionar.text = if (dialogType == ADD_LIST) {
            "Adicionar"
        } else {
            "Atualizar"
        }

        btnAdicionar.isEnabled = false

        btnAdicionar.setOnClickListener {
            addButtonClicked(etNomeLista.text.toString())
            dismiss()
        }

        btnCancelar.setOnClickListener {
            dismiss()
        }

        etNomeLista.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(text: CharSequence, start: Int, before: Int, count: Int) {
                val listaExistente = listAlreadyAdded.invoke(text.trim().toString())

                if (listaExistente) {
                    tilNomeLista.isErrorEnabled = true
                    tilNomeLista.error = getString(R.string.msg_lista_existente)
                } else {
                    tilNomeLista.error = null
                    tilNomeLista.isErrorEnabled = false
                }

                btnAdicionar.isEnabled = text.isNotEmpty() && !listaExistente
            }

        })

    }
}