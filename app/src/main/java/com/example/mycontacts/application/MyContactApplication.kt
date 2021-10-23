package com.example.mycontacts.application

import android.app.Application
import com.example.mycontacts.model.database.MyContactRepository
import com.example.mycontacts.model.database.MyContactRoomDatabase

class MyContactApplication : Application() {

    private val database by lazy {MyContactRoomDatabase.getDatabase((this@MyContactApplication))}
    val repository by lazy { MyContactRepository(database.myContactDao()) }
}