package com.con19.tripplanner.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.con19.tripplanner.db.AppDatabase
import com.con19.tripplanner.db.entities.Transaction
import com.con19.tripplanner.model.TransactionService
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class TransactionViewModel(application: Application) : AndroidViewModel(application) {

    private val service: TransactionService

    init {
        val transactionDao = AppDatabase.getDatabase(application, viewModelScope).transactionDao()
        service = TransactionService.getInstance(transactionDao)
    }

    fun getTransactionsForTrip(tripId: Long) = service.getTransactionsForTripWithId(tripId)


    /**
     * Launches a new coroutine to insert the data in a non-blocking way
     * viewModelScope is automatically cancelled when the ViewModel is cleared.
     * @return a deferred reference to the id of the inserted. get actual id by using .await()
     */
    fun insertAsync(transaction: Transaction): Deferred<Long> = viewModelScope.async(Dispatchers.IO) {
        service.insert(transaction)
    }

    fun addPersonToTransaction(transactionId: Long, personId: Long) = viewModelScope.launch(Dispatchers.IO) {
        service.addPersonToTransaction(transactionId, personId)
    }


    fun removePersonFromTransaction(transactionId: Long, personId: Long) = viewModelScope.launch(Dispatchers.IO) {
        service.removePersonFromTransaction(transactionId, personId)
    }

    fun changeTransactionPaidStatus(transactionId: Long, newPaidStatus: Boolean) {
        service.changeTransactionPaidStatus(transactionId, newPaidStatus)
    }
}