package com.example.mycontacts.model.database

import androidx.annotation.WorkerThread
import com.example.mycontacts.model.entities.MyContact
import com.example.mycontacts.model.entities.RecentContact
import kotlinx.coroutines.flow.Flow

class MyContactRepository(private val myContactDao: MyContactDao) {
    @WorkerThread
    suspend fun insertMyContact(myContact: MyContact){
        myContactDao.insertMyContactDetails(myContact)
    }

    val allContactsList: Flow<List<MyContact>> = myContactDao.getAllContacts()

    @WorkerThread
    suspend fun updateMyContactData(myContact: MyContact){
        myContactDao.updateMyContactDetails(myContact)
    }

    val favContactList: Flow<List<MyContact>> = myContactDao.getFavouriteContact()

    @WorkerThread
    suspend fun deleteMyContactData(myContact: MyContact){
        myContactDao.deleteContactDetails(myContact)
    }

    fun searchDatabase(searchQuery: String): Flow<List<MyContact>> {
        return myContactDao.searchDatabase(searchQuery)
    }

    fun searchFavDatabase(searchQuery: String): Flow<List<MyContact>> {
        return myContactDao.searchFavDatabase(searchQuery)
    }

    @WorkerThread
    suspend fun insertMyRecentContact(myContact: RecentContact){
        myContactDao.insertMyRecentContactDetails(myContact)
    }

    fun searchRecentDatabase(searchQuery: String): Flow<List<MyContact>> {
        return myContactDao.searchRecentDatabase(searchQuery)
    }

    val allRecentContactsList: Flow<List<MyContact>> = myContactDao.getAllRecentContacts()

    @WorkerThread
    suspend fun deleteMyRecentContactData(){
        myContactDao.deleteRecentContactDetails()
    }
}