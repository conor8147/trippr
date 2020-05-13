package com.con19.tripplanner.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.con19.tripplanner.db.entities.Person
import com.con19.tripplanner.db.entities.Transaction
import com.con19.tripplanner.db.entities.Trip
import com.con19.tripplanner.db.dao.PersonDao
import com.con19.tripplanner.db.dao.TransactionDao
import com.con19.tripplanner.db.dao.TripDao
import com.con19.tripplanner.db.entities.Converters
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(entities = [Person::class, Transaction::class, Trip::class], version = 1)
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

        override fun onOpen(db: SupportSQLiteDatabase) {
            super.onOpen(db)
            INSTANCE?.let { database ->
                scope.launch {
                    populatePeopleDatabase(database.personDao())
                }
            }
        }

        suspend fun populatePeopleDatabase(personDao: PersonDao) {
            // Delete all content here.
            personDao.deleteAll()

            val testPeople = listOf(
                Person("Conor", "0273785420"),
                Person("Nick", "24532"),
                Person("Angus", "4323432"),
                Person("Charlotte", "0273785420"),
                Person("James", "24532"),
                Person("Ted", "4323432"),
                Person("Sam", "4323432")
            )

            testPeople.forEach {
                personDao.insert(it)
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

