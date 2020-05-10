package com.con19.tripplanner.model

import androidx.lifecycle.LiveData
import com.con19.tripplanner.db.dao.PersonDao
import com.con19.tripplanner.db.entities.Person

/**
 * Central repository for getting Person data from the database.
 */
class PersonService(private val personDao: PersonDao) {

    val allPeople: LiveData<List<Person>> by lazy { personDao.getAll() }

    fun getPersonById(id: Int) = personDao.getPersonById(id)

    suspend fun insert(person: Person) = personDao.insert(person)

    companion object {
        private var INSTANCE: PersonService? = null

        /**
         * Ensure that we only have single instance of the data repository
         */
        fun getInstance(personDao: PersonDao): PersonService {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            // synchronized just means that two of these can't happen concurrently, resulting in two instances.
            synchronized(this) {
                val instance = PersonService(personDao)
                INSTANCE = instance
                return instance
            }
        }
    }

}