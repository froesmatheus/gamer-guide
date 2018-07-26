package com.matheusfroes.gamerguide

import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.dialog_adicionar_lista.*

class AddGameListDialog : DialogFragment() {

    private lateinit var addButtonClicked: ((String) -> Unit)
    private lateinit var listAlreadyAdded: ((String) -> Boolean)

    fun addButtonClick(listener: ((String) -> Unit)) {
        this.addButtonClicked = listener
    }

    fun listAlreadyAdded(listener: ((String) -> Boolean)) {
        this.listAlreadyAdded = listener
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
            inflater.inflate(R.layout.dialog_adicionar_lista, container, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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

//    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
//        val view = View.inflate(activity, R.layout.dialog_adicionar_lista, null)
//
//        val dialog = AlertDialog.Builder(activity)
//                .setView(view)
//                .setPositiveButton(getString(R.string.adicionar)) { _, _ ->
//                    addButtonClicked.invoke(view.etNomeLista.text.toString())
//                }
//                .setNegativeButton(getString(R.string.cancelar)) { _, _ -> }
//                .create()
//
//
//        val botaoAdicionar = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
//
//        botaoAdicionar.isEnabled = false
//
//        view.etNomeLista.addTextChangedListener(object : TextWatcher {
//            override fun afterTextChanged(p0: Editable?) {}
//
//            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
//
//            override fun onTextChanged(text: CharSequence, p1: Int, p2: Int, p3: Int) {
//                val listaExistente = listAlreadyAdded.invoke(text.trim().toString())
//
//                if (listaExistente) {
//                    view.tilNomeLista.isErrorEnabled = true
//                    view.tilNomeLista.error = getString(R.string.msg_lista_existente)
//                } else {
//                    view.tilNomeLista.error = null
//                    view.tilNomeLista.isErrorEnabled = false
//                }
//
//                botaoAdicionar.isEnabled = text.isNotEmpty() && !listaExistente
//            }
//        })
//
//        return dialog
//    }
}