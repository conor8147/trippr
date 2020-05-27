package com.con19.tripplanner.view.fragments

import android.content.res.Resources
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.core.net.toFile
import androidx.lifecycle.lifecycleScope
import com.con19.tripplanner.R
import com.con19.tripplanner.db.entities.Person
import com.con19.tripplanner.db.entities.Transaction
import com.con19.tripplanner.db.entities.TransactionWithPeople
import com.google.android.material.chip.Chip
import kotlinx.coroutines.launch
import java.io.File
import kotlin.properties.Delegates


/**
 * A simple extension of Add Receipt that pre-populates the layout fields with the receipt details and
 * updates the current transaction instead of adding a new one on submit.
 */
class EditReceiptFragment : AddReceiptFragment() {

    private var transactionId by Delegates.notNull<Long>()
    private lateinit var transactionWithPeople: TransactionWithPeople
    private lateinit var transaction: Transaction
    private lateinit var peopleOnTransaction: List<Person>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            transactionId = it.getLong(TRANSACTION_ID)
        }

        lifecycleScope.launch { getTransaction() }
    }

    override fun setToolbarListeners() {
        super.setToolbarListeners()
        toolbar.apply {
            title = context.getString(R.string.edit_receipt)
        }
    }

    private suspend fun getTransaction() {
        transactionWithPeople = transactionViewModel.getTransactionById(transactionId) ?:
                // TODO: handle this gracefully
                throw Resources.NotFoundException()
        transaction = transactionWithPeople.transaction
        peopleOnTransaction = transactionWithPeople.people
        nameEditText.setText(transaction.name)
        costEditText.setText(transaction.cost.toString())
        chipGroup.removeAllViews()
        peopleOnTransaction.forEach { person ->
            addedPeople.add(person)
            val chip = Chip(requireContext())
            chip.text = person.nickname
            chipGroup.addView(chip)
        }
        if (!transaction.image.isNullOrEmpty()) {
            receiptImageView.setImageURI(Uri.parse(transaction.image))
        }
    }

    override fun submitTransaction() {
        //TODO deal with wrong number of decimal places or null cost
        val price = costEditText.text.toString().toFloatOrNull()
        val name = nameEditText.text.toString()
        if (price != null) {
            if (!receiptPhotoUri.isNullOrEmpty()) {
                deletePhoto(transaction.image)
                transaction.image = receiptPhotoUri
            }
            transaction.cost = price
            transaction.name = name
            transactionViewModel.update(transaction, addedPeople)
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