package com.example.mycontacts.model.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room.databaseBuilder
import androidx.room.RoomDatabase
import com.example.mycontacts.model.entities.MyContact
import com.example.mycontacts.model.entities.RecentContact

@Database(entities = [MyContact::class, RecentContact::class], version = 3)
abstract class MyContactRoomDatabase : RoomDatabase() {

    abstract fun myContactDao(): MyContactDao

    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: MyContactRoomDatabase? = null

        fun getDatabase(context: Context): MyContactRoomDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = databaseBuilder(
                    context.applicationContext,
                    MyContactRoomDatabase::class.java,
                    "my_contact_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}