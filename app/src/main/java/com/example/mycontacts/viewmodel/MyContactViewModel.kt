package com.example.mycontacts.viewmodel

import androidx.lifecycle.*
import com.example.mycontacts.model.database.MyContactRepository
import com.example.mycontacts.model.entities.MyContact
import com.example.mycontacts.model.entities.RecentContact
import kotlinx.coroutines.launch

class MyContactViewModel(private val repository : MyContactRepository) : ViewModel(){
    fun insert (contact: MyContact) = viewModelScope.launch{
        repository.insertMyContact(contact)
    }
    val allContactsList: LiveData<List<MyContact>> = repository.allContactsList.asLiveData()

    fun update(myContact: MyContact) = viewModelScope.launch {
        repository.updateMyContactData(myContact)
    }

    val favouriteContacts: LiveData<List<MyContact>> = repository.favContactList.asLiveData()

    fun delete(myContact: MyContact) = viewModelScope.launch {
        repository.deleteMyContactData(myContact)
    }

    fun searchDatabase(searchQuery: String): LiveData<List<MyContact>> {
        return repository.searchDatabase(searchQuery).asLiveData()
    }

    fun searchFavDatabase(searchQuery: String): LiveData<List<MyContact>> {
        return repository.searchFavDatabase(searchQuery).asLiveData()
    }

    fun searchRecentDatabase(searchQuery: String): LiveData<List<MyContact>> {
        return repository.searchRecentDatabase(searchQuery).asLiveData()
    }

    fun insertRecent (contact: RecentContact) = viewModelScope.launch{
        repository.insertMyRecentContact(contact)
    }
    val allRecentContactsList: LiveData<List<MyContact>> = repository.allRecentContactsList.asLiveData()

    fun deleteRecent() = viewModelScope.launch {
        repository.deleteMyRecentContactData()
    }
}

class MyContactViewModelFactory(private val repository: MyContactRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MyContactViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MyContactViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}