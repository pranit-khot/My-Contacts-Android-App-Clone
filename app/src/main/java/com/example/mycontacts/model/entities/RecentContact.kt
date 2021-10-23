package com.example.mycontacts.model.entities

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "my_recent_table")
data class RecentContact(
    @ColumnInfo(name = "original_id") val originalID: Int,
    @PrimaryKey(autoGenerate = true) val id: Int = 0
): Parcelable
