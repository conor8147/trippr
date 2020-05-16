package com.con19.tripplanner.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.con19.tripplanner.db.AppDatabase
import com.con19.tripplanner.db.entities.Person
import com.con19.tripplanner.model.PersonService
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async

/**
 * Provides methods for accessing the PersonDao
 */
class PersonViewModel(application: Application): AndroidViewModel(application) {

    val service: PersonService

    // Using LiveData and caching what allPeople returns has several benefits:
    //   - We can put an observer on the data (instead of polling for changes) and only update the
    //     the UI when the data actually changes.
    //   - Repository is completely separated from the UI through the ViewModel.
    val allPeople: LiveData<List<Person>>

    init {
        val personDao = AppDatabase.getDatabase(application, viewModelScope).personDao()
        service = PersonService.getInstance(personDao)
        allPeople = service.allPeople
    }

    /**
     * Launches a new coroutine to insert the data in a non-blocking way
     * viewModelScope is automatically cancelled when the ViewModel is cleared.
     * @return a deferred reference to the id of the inserted. get actual id by using .await()
     */
    fun insertAsync(person: Person): Deferred<Long> = viewModelScope.async(Dispatchers.IO) {
        service.insert(person)
    }

    fun getPersonById(personId: Long): Person? {
        return allPeople.value?.find { person ->
            person.personId == personId
        }
    }
}