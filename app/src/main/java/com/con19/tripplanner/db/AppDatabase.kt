package com.con19.tripplanner.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.con19.tripplanner.db.dao.PersonDao
import com.con19.tripplanner.db.dao.TransactionDao
import com.con19.tripplanner.db.dao.TripDao
import com.con19.tripplanner.db.entities.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.util.*

@Database(
    entities = [Person::class,
        Transaction::class,
        Trip::class,
        TripPersonCrossRef::class,
        TransactionPersonCrossRef::class
    ], version = 1
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun personDao(): PersonDao
    abstract fun transactionDao(): TransactionDao
    abstract fun tripDao(): TripDao

    /**
     * Test class for adding database data.
     * TODO: Delete this, and remove databaseBuilder.addCallback below / remove scope param in getDatabase/references to getDatabase
     *
     */
    private class AppDatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {

        private val personIds = mutableListOf<Long>()
        private val tripIds = mutableListOf<Long>()
        private val transactionIds = mutableListOf<Long>()

        override fun onOpen(db: SupportSQLiteDatabase) {
            super.onOpen(db)
            INSTANCE?.let { database ->
                scope.launch {
                    populatePeopleDatabase(database.personDao())
                    populateTripDatabase(database.tripDao())
                    populateTransactionsDatabase(database.transactionDao())
                }
            }
        }

        suspend fun populatePeopleDatabase(personDao: PersonDao) {
            // Delete all content here.
            personDao.deleteAll()

            val testPeople = listOf(
                Person("Conor", "0273785420"),
                Person("Nick", "0220999999"),
                Person("Angus", "021456890"),
                Person("Charlotte", "0273785420"),
                Person("James", "027123478"),
                Person("Ted", "0223456789"),
                Person("Sam", "0213428596"),
                Person("Kevin", "021232321"),
                Person("Bobby", "021888888")
            )

            testPeople.forEach {
                personIds.add(personDao.insert(it))
            }
        }

        suspend fun populateTripDatabase(tripDao: TripDao) {
            // Delete all content here.
            tripDao.deleteAll()

            val testTrips = listOf(
                Trip("Ski Trip", Date(2020, 3, 12), Date(2020, 3, 25), null),
                Trip("Friday Drinks", Date(2020, 1, 1), Date(2020, 6, 1), null),
                Trip("Weekend Climb", Date(2020, 4, 13), Date(2020, 4, 16), null),
                Trip("Hanmer Springs", Date(2020, 2, 2), Date(2020, 2, 3), null),
                Trip("West Coast", Date(2019, 12, 20), Date(2019, 12, 31), null)

            )

            testTrips.forEach {
                tripIds.add(tripDao.insert(it))
            }

            personIds.subList(0,3).forEach {personId ->
                tripIds.forEach {tripId ->
                    tripDao.insert(TripPersonCrossRef(tripId, personId))
                }
            }
        }

        suspend fun populateTransactionsDatabase(transactionDao: TransactionDao) {
            val testTransactions = listOf(
                Transaction("Dinner @ Moes", Date(), tripIds[0], false, 150.59F, null),
                Transaction("Ski hire", Date(), tripIds[0], true, 200.00F, null),
                Transaction("Lunch @ Coronet", Date(), tripIds[0], false, 45.80F, null),
                Transaction("Booze", Date(), tripIds[0], false, 33.59F, null),
                Transaction("Girls", Date(), tripIds[0], false, 40.50F, null),
                Transaction("The Devil's Lettuce", Date(), tripIds[0], true, 42.0F, null),
                Transaction("Dinner @ Burger Burger", Date(), tripIds[0], false, 73.59F, null),
                Transaction("Mondays Groceries", Date(), tripIds[0], true, 15.99F, null),
                Transaction("Bubble Tea", Date(), tripIds[0], true, 6.50F, null),
                Transaction("Fish and Chips", Date(), tripIds[0], false, 25.99F, null),
                Transaction("Breakfast @ Heritage", Date(), tripIds[0], false, 189.99F, null)
            )

            testTransactions.forEach {
                transactionIds.add(transactionDao.insert(it))
            }

            transactionIds.forEach { transactionId ->
                personIds.forEach { personId ->
                    transactionDao.insert(TransactionPersonCrossRef(transactionId, personId))
                }
            }
        }
    }

    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(
            context: Context,
            scope: CoroutineScope
        ): AppDatabase {
            val tempInstance =
                INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        "app_database"
                    )
                    .addCallback(AppDatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }

}


