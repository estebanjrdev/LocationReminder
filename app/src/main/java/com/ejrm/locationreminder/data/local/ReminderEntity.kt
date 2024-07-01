package com.ejrm.locationreminder.data.local


import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "reminders")
data class ReminderEntity(
    @PrimaryKey val id: String,
    val name: String,
    val latitude: Double,
    val longitude: Double,
    val radius: Float,
    val activationDate: Date? = null
)
