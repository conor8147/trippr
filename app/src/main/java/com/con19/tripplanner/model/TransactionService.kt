package com.con19.tripplanner.model

import com.con19.tripplanner.db.dao.TransactionDao
import com.con19.tripplanner.db.entities.Transaction

/**
 * Central repository for getting Person data from the database.
 */
class TransactionService(private val transactionDao: TransactionDao) {

    fun getTransactionsForTrip(tripId: Int) = transactionDao.getTransactionsForTrip(tripId)

    fun getTransactionById(transactionId: Int) = transactionDao.getTransactionById(transactionId)

    suspend fun insert(transaction: Transaction) = transactionDao.insert(transaction)

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