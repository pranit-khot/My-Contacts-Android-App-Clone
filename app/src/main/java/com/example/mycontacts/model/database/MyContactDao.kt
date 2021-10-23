package com.example.mycontacts.model.database

import androidx.room.*
import com.example.mycontacts.model.entities.MyContact
import com.example.mycontacts.model.entities.RecentContact
import kotlinx.coroutines.flow.Flow


@Dao
interface MyContactDao {
    @Insert
    suspend fun insertMyContactDetails(myContact: MyContact)

    @Query("SELECT * FROM MY_CONTACTS_TABLE ORDER BY FIRST_NAME")
    fun getAllContacts(): Flow<List<MyContact>>

    @Update
    suspend fun updateMyContactDetails(myContact: MyContact)

    @Query("SELECT * FROM MY_CONTACTS_TABLE WHERE IS_FAVOURITE = 1 ORDER BY FIRST_NAME")
    fun getFavouriteContact(): Flow<List<MyContact>>

    @Delete
    suspend fun deleteContactDetails(myContact: MyContact)

    @Query("SELECT * FROM MY_CONTACTS_TABLE WHERE FIRST_NAME LIKE :searchQuery OR LAST_NAME LIKE :searchQuery ORDER BY FIRST_NAME")
    fun searchDatabase(searchQuery: String): Flow<List<MyContact>>

    @Query("SELECT * FROM MY_CONTACTS_TABLE WHERE IS_FAVOURITE = 1 AND (FIRST_NAME LIKE :searchQuery OR LAST_NAME LIKE :searchQuery) ORDER BY FIRST_NAME")
    fun searchFavDatabase(searchQuery: String): Flow<List<MyContact>>

    @Insert
    suspend fun insertMyRecentContactDetails(myContact: RecentContact)

    @Query("SELECT * FROM MY_CONTACTS_TABLE INNER JOIN my_recent_table ON MY_CONTACTS_TABLE.ID = my_recent_table.original_id ORDER BY my_recent_table.id DESC")
    fun getAllRecentContacts(): Flow<List<MyContact>>

    @Query("SELECT * FROM MY_CONTACTS_TABLE INNER JOIN my_recent_table ON MY_CONTACTS_TABLE.ID = my_recent_table.original_id WHERE MY_CONTACTS_TABLE.FIRST_NAME LIKE :searchQuery OR MY_CONTACTS_TABLE.LAST_NAME LIKE :searchQuery ORDER BY my_recent_table.id DESC")
    fun searchRecentDatabase(searchQuery: String): Flow<List<MyContact>>

    @Query("DELETE FROM MY_RECENT_TABLE")
    suspend fun deleteRecentContactDetails()
}