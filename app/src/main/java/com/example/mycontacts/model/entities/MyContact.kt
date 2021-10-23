package com.example.mycontacts.model.entities

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "my_contacts_table")
data class MyContact (
    @ColumnInfo val image: String,
    @ColumnInfo(name = "first_name") val firstName: String,
    @ColumnInfo(name = "middle_name") val middleName: String,
    @ColumnInfo(name = "last_name") val lastName: String,
    @ColumnInfo(name = "work_number") val workNumber: String,
    @ColumnInfo(name = "home_number") val homeNumber: String,
    @ColumnInfo(name = "other_number") val otherNumber: String,
    @ColumnInfo val email :String,
    @ColumnInfo val company :String,
    @ColumnInfo(name = "is_favourite") var isFavourite: Boolean = false,
    @PrimaryKey(autoGenerate = true) val id: Int = 0
) : Parcelable