package com.con19.tripplanner.view.fragments

import android.content.res.Resources
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.con19.tripplanner.db.entities.Person
import com.con19.tripplanner.db.entities.Transaction
import com.con19.tripplanner.db.entities.TransactionWithPeople
import com.google.android.material.chip.Chip
import kotlinx.coroutines.launch
import kotlin.properties.Delegates

/**
 * A simple extension of Add Receipt that pre-populates the layout fields with the receipt details and
 * updates the current transaction instead of adding a new one on submit.
 */
class EditReceiptFragment() : AddReceiptFragment() {

    private var transactionId by Delegates.notNull<Long>()
    private var transactionWithPeople: TransactionWithPeople? = null
    private var transaction: Transaction? = null
    private var peopleOnTransaction: List<Person>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            transactionId = it.getLong(TRANSACTION_ID)
        }

        lifecycleScope.launch{ getTransaction() }
    }

    private suspend fun getTransaction() {
        transactionWithPeople =
            transactionViewModel.getTransactionById(transactionId) ?:
            // TODO: handle this gracefully
            throw Resources.NotFoundException()
        transaction = transactionWithPeople?.transaction
        peopleOnTransaction = transactionWithPeople?.people
        nameEditText.setText(transaction?.name)
        costEditText.setText(transaction?.cost.toString())
        chipGroup.removeAllViews()
        peopleOnTransaction?.forEach { person ->
            val chip = Chip(context)
            chip.text = person.nickname
            chipGroup.addView(chip)
        }
    }


    override fun submitTransaction() {
        //TODO deal with wrong number of decimal places or null cost
        val price = costEditText.text.toString().toFloatOrNull()
        val name = nameEditText.text.toString()
        if (price != null) {
            transaction?.cost = price
            transaction?.name
            transaction?.let {
                transactionViewModel.update(it, addedPeople)
            }
            listener?.onReceiptFragmentBackButtonPressed(tripId)
        }
    }

    companion object {
        private const val TRANSACTION_ID = "transactionId"

        @JvmStatic
        fun newInstance(tripId: Long, transactionId: Long) =
            EditReceiptFragment().apply {
                arguments = Bundle().apply {
                    putLong(TRIP_ID, tripId)
                    putLong(TRANSACTION_ID, transactionId)
                }
            }
    }
}