package com.farhanck.currencyconverter.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.farhanck.currencyconverter.R
import com.farhanck.currencyconverter.data.db.ExchangeRate
import com.farhanck.currencyconverter.data.db.Quote
import kotlinx.android.synthetic.main.exchange_item.view.*
import java.math.RoundingMode
import java.text.DecimalFormat

class ExchangeAdapter(var quotes:List<Quote>) : RecyclerView.Adapter<ExchangeHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExchangeHolder {
        return ExchangeHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.exchange_item,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return  quotes.size
    }

    override fun onBindViewHolder(holder: ExchangeHolder, position: Int) {
        holder.bind(quotes.get(position));
    }

}

class ExchangeHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val df = DecimalFormat("#.##")

    init {
        df.roundingMode = RoundingMode.CEILING
    }
    fun bind(quote : Quote) {
        itemView.currency.text = quote.currency;
        itemView.rate.text = df.format(quote.rate);
    }

}


@BindingAdapter(value = ["rate", "viewModel"])
fun setExchangeRates(view: RecyclerView, rate: ExchangeRate?, vm: ConverterViewModel) {
    val rate = rate ?: return;
    view.adapter?.run {
        if (this is ExchangeAdapter) {
            this.quotes = rate.quoteList
            this.notifyDataSetChanged()
        }
    } ?: run {
        ExchangeAdapter(rate.quoteList).apply {
            ExchangeAdapter(rate.quoteList).apply { view.adapter = this }
        }
    }
}