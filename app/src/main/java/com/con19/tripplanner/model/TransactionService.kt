package com.con19.tripplanner.model

import androidx.lifecycle.LiveData
import com.con19.tripplanner.db.dao.TransactionDao
import com.con19.tripplanner.db.entities.Person
import com.con19.tripplanner.db.entities.Transaction
import com.con19.tripplanner.db.entities.TransactionPersonCrossRef
import com.con19.tripplanner.db.entities.TransactionWithPeople

/**
 * Central repository for getting Person data from the database.
 */
class TransactionService(private val transactionDao: TransactionDao) {

    fun getTransactionsForTripWithId(tripId: Long): LiveData<List<TransactionWithPeople>> =
        transactionDao.getTransactionsForTripWithId(tripId)

    suspend fun insert(transaction: Transaction, people: List<Person>): Long {
        val transactionId = transactionDao.insert(transaction)
        people.forEach {
            transactionDao.insert(
                TransactionPersonCrossRef(
                    transactionId,
                    it.personId
                )
            )
        }
        return transactionId
    }

    suspend fun addPersonToTransaction(transactionId: Long, personId: Long) {
        transactionDao.insert(
            TransactionPersonCrossRef(
                transactionId,
                personId
            )
        )
    }

    suspend fun removePersonFromTransaction(transactionId: Long, personId: Long) {
        transactionDao.delete(
            TransactionPersonCrossRef(
                transactionId,
                personId
            )
        )
    }

    fun changeTransactionPaidStatus(transactionId: Long, newPaidStatus: Boolean) {
        transactionDao.updatePaidStatusForTransaction(transactionId, newPaidStatus)
    }

    companion object {
        private var INSTANCE: TransactionService? = null

        /**
         * Ensure that we only have single instance of the Transaction Service (Singleton)
         */
        fun getInstance(transactionDao: TransactionDao): TransactionService {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            // synchronized just means that two of these can't happen concurrently, resulting in two instances.
            synchronized(this) {
                val instance = TransactionService(transactionDao)
                INSTANCE = instance
                return instance
            }
        }
    }
}