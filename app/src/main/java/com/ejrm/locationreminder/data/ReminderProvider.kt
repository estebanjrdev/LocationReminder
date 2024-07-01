package com.ejrm.locationreminder.data

import com.ejrm.locationreminder.data.model.ReminderModel
import kotlinx.coroutines.flow.Flow

interface ReminderProvider {
    suspend fun addReminder(reminder: ReminderModel)
    suspend fun getReminders(): Flow<List<ReminderModel>>
    suspend fun getReminderById(id: String): ReminderModel?
}
