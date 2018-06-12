package com.matheusfroes.gamerguide.ui.detalhesjogo

import android.content.Context
import android.net.Uri
import android.support.customtabs.CustomTabsIntent
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.matheusfroes.gamerguide.R
import com.matheusfroes.gamerguide.network.data.Product
import com.matheusfroes.gamerguide.network.data.Stream
import kotlinx.android.synthetic.main.view_oferta.view.*
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.NumberFormat

class OfertasAdapter(val context: Context) : RecyclerView.Adapter<OfertasAdapter.ViewHolder>() {
    private var ofertas: MutableList<Product> = mutableListOf()
    private var listener: OnStreamClickListener? = null

    fun preencherLista(listas: List<Product>) {
        this.ofertas.addAll(listas)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
            ViewHolder(LayoutInflater.from(context).inflate(R.layout.view_oferta, parent, false))

    override fun getItemCount() = ofertas.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val oferta = ofertas[position]

        val df = NumberFormat.getCurrencyInstance()
        val dfs = DecimalFormatSymbols()
        dfs.currencySymbol = ""
        dfs.groupingSeparator = ','
        dfs.monetaryDecimalSeparator = ','
        (df as DecimalFormat).decimalFormatSymbols = dfs

        holder.itemView.tvDescricaoOferta.text = oferta.name
        holder.itemView.tvPrecoOferta.text = "R$ ${df.format(oferta.priceMin)}"
    }

    fun setOnStreamClickListener(listener: OnStreamClickListener) {
        this.listener = listener
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        init {
            itemView.btnComprar.setOnClickListener {
                val oferta = ofertas[adapterPosition]

                val customTabsIntent = CustomTabsIntent.Builder()
                        .setToolbarColor(ContextCompat.getColor(context, R.color.colorPrimary))
                        .build()
                customTabsIntent.launchUrl(context, Uri.parse(oferta.link))
            }
//            itemView.setOnClickListener {
//                val stream = ofertas[adapterPosition]
//                listener?.onStreamClick(stream)
//            }
        }

    }

    interface OnStreamClickListener {
        fun onStreamClick(stream: Stream)
    }
}