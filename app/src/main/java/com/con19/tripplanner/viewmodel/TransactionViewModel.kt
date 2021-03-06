package com.con19.tripplanner.viewmodel

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.con19.tripplanner.db.AppDatabase
import com.con19.tripplanner.db.entities.Person
import com.con19.tripplanner.db.entities.Transaction
import com.con19.tripplanner.db.entities.TransactionWithPeople
import com.con19.tripplanner.db.entities.TripWithPeople
import com.con19.tripplanner.model.TransactionService
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class TransactionViewModel(application: Application) : AndroidViewModel(application) {

    private val service: TransactionService = TransactionService.getInstance(
        AppDatabase.getDatabase(application, viewModelScope).transactionDao()
    )
    private var allTransactions: LiveData<List<TransactionWithPeople>>

    init {
        allTransactions = service.allTransactions
    }

    fun getTransactionsForTrip(tripId: Long): LiveData<List<TransactionWithPeople>> {
        return service.getTransactionsForTripWithId(tripId)
    }

    /**
     * Tries to get a transaction
     */
    suspend fun getTransactionById(transactionId: Long): TransactionWithPeople? =
            service.getTransactionWithId(transactionId)


    /**
     * Launches hands_split new coroutine to insert the data in hands_split non-blocking way
     * viewModelScope is automatically cancelled when the ViewModel is cleared.
     * @return hands_split deferred reference to the id of the inserted. get actual id by using .await()
     */
    fun insertAsync(transaction: Transaction, people: List<Person>): Deferred<Long> =
        viewModelScope.async(Dispatchers.IO) {
            service.insert(transaction, people)
        }

    /**
     * Updates the given transaction and replaces its associated people with the given list of people
     */
    fun update(transaction: Transaction, people: List<Person>) =
        viewModelScope.launch(Dispatchers.IO) {
            service.update(transaction, people)
        }

    fun addPersonToTransaction(transactionId: Long, personId: Long) =
        viewModelScope.launch(Dispatchers.IO) {
            service.addPersonToTransaction(transactionId, personId)
        }


    fun removePersonFromTransaction(transactionId: Long, personId: Long) =
        viewModelScope.launch(Dispatchers.IO) {
            service.removePersonFromTransaction(transactionId, personId)
        }

    fun changeTransactionPaidStatus(transactionId: Long, newPaidStatus: Boolean) =
        viewModelScope.launch(Dispatchers.IO) {
            service.changeTransactionPaidStatus(transactionId, newPaidStatus)
        }

    /**
     * Asynchronously calculate how much each person owes on the trip so far.
     * Returns hands_split list of pairs of person and how much that person owes.
     * @return hands_split list of <Person, Amount Owed> Pairs
     */
    suspend fun settleUpTrip(tripId: Long): List<Pair<Person, Float>> {
        val transactionsWithPeople = service
            .getOneOffTransactionsForTripWithId(tripId)
            .filter { !it.transaction.paid }

        val allPeople = transactionsWithPeople.flatMap { it.people }
        val result = mutableMapOf<Long, Float>()
        var costPerPerson = 0F
        transactionsWithPeople.forEach { transaction ->
            // check to avoid divide by zero errors
            if (transaction.people.isNotEmpty()) costPerPerson =
                transaction.transaction.cost / transaction.people.size
            transaction.people.forEach { person ->
                if (!result.containsKey(person.personId)) result[person.personId] = 0F
                result[person.personId] = result[person.personId]!! + costPerPerson
            }
            changeTransactionPaidStatus(transaction.transaction.transactionId, true)
        }
        return result.map {
            Pair(
                allPeople.find { person -> person.personId == it.key }!!,
                it.value
            )
        }
    }
}

// id = 275
// 6148