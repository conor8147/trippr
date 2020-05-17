package com.con19.tripplanner.view.adapters

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
    context: Context
) : RecyclerView.Adapter<TransactionsAdapter.TransactionViewHolder>(){

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    //var onPersonClickedListener: PeopleAdapter.OnPersonClickedListener? = null
    //private var listener: TransactionAdapter.OnTransactionClickedListener? = null

    var transactionList = emptyList<TransactionWithPeople>() // Cached copy of people
        set(transactionList) {
            field = transactionList
            notifyDataSetChanged()
        }


    class TransactionViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView = view.textView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        val transactionCard = inflater.inflate(R.layout.view_basic_card, parent, false)
        return TransactionViewHolder(transactionCard)
    }

    override fun getItemCount(): Int = transactionList.size

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        holder.textView.text = transactionList[position].transaction.name
    }
}