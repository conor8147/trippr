package com.con19.tripplanner.view.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.con19.tripplanner.R
import com.con19.tripplanner.db.entities.TransactionWithPeople
import kotlinx.android.synthetic.main.view_basic_card.view.*

class TransactionsAdapter internal constructor(
    val context: Context
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var paidTransactions = emptyList<TransactionWithPeople>()
    private var unPaidTransactions = emptyList<TransactionWithPeople>()
    var allTransactions = emptyList<TransactionWithPeople>()
        set(transactions) {
            field = transactions
            updateTransactions()
            notifyDataSetChanged()
        }

    init {
        updateTransactions()
    }

    class TransactionViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView = view.textView
    }

    class HeaderViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        //val textView = view.includedLayout.textView
    }

    override fun getItemViewType(position: Int): Int = when {
        position == getHeaderPosition() -> HEADER_VIEW
        position == getUnpaidTitlePosition() -> TITLE_CARD_VIEW
        position == getPaidTitlePosition() -> TITLE_CARD_VIEW
        else -> BASIC_CARD_VIEW
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            HEADER_VIEW -> HeaderViewHolder(inflater.inflate(R.layout.view_trip_view_info, parent, false))
            TITLE_CARD_VIEW -> TransactionViewHolder(inflater.inflate(R.layout.view_title_card, parent, false))
            else -> TransactionViewHolder(inflater.inflate(R.layout.view_basic_card, parent, false))
        }
    }

    // Enough for all transactions, plus two title cards
    override fun getItemCount(): Int = paidTransactions.size + unPaidTransactions.size + 2 + 1

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is HeaderViewHolder) {
            // set all text
        } else if (holder is TransactionViewHolder) {
            holder.textView.text = when {
                position == getUnpaidTitlePosition() -> {
                    context.getString(R.string.unpaid) + ": "
                }
                position == getPaidTitlePosition() -> {
                    context.getString(R.string.paid) + ": "
                }
                position <= getPaidTitlePosition() -> {
                    unPaidTransactions[position - (getUnpaidTitlePosition() + 1) ].transaction.name
                }
                else -> {
                    holder.textView.alpha = 0.3F
                    holder.view.imageView.alpha = 0.3F
                    paidTransactions[position - (getPaidTitlePosition() + 1) ].transaction.name
                }
            }
        }
    }

    private fun updateTransactions() {
        paidTransactions = allTransactions.filter {
            it.transaction.paid
        }

        unPaidTransactions = allTransactions.filter {
            !it.transaction.paid
        }
    }

    private fun getHeaderPosition(): Int = 0

    private fun getUnpaidTitlePosition(): Int = 1

    private fun getPaidTitlePosition(): Int = unPaidTransactions.size + 2

    companion object {
        const val HEADER_VIEW = 0
        const val TITLE_CARD_VIEW = 1
        const val BASIC_CARD_VIEW = 2
    }
}