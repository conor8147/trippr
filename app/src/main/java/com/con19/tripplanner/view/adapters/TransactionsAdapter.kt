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
) : RecyclerView.Adapter<TransactionsAdapter.TransactionViewHolder>() {

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

    override fun getItemViewType(position: Int): Int = when {
        position == 0 -> TITLE_CARD_VIEW
        position == paidTransactions.size + 1 -> TITLE_CARD_VIEW
        position <= paidTransactions.size -> BASIC_CARD_VIEW
        else -> BASIC_CARD_VIEW
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        val view = when (viewType) {
            TITLE_CARD_VIEW -> R.layout.view_title_card
            else -> R.layout.view_basic_card
        }
        val card = inflater.inflate(view, parent, false)
        return TransactionViewHolder(card)
    }

    // Enough for all transactions, plus two title cards
    override fun getItemCount(): Int = paidTransactions.size + unPaidTransactions.size + 2

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        holder.textView.text = when {
            position == 0 -> {
                context.getString(R.string.paid) + ": "
            }
            position == paidTransactions.size + 1-> {
                context.getString(R.string.unpaid) + ": "
            }
            position <= paidTransactions.size -> {
                paidTransactions[position - 1].transaction.name
            }
            else -> {
                unPaidTransactions[position - paidTransactions.size - 2].transaction.name
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

    companion object {
        const val TITLE_CARD_VIEW = 0
        const val BASIC_CARD_VIEW = 1
    }
}