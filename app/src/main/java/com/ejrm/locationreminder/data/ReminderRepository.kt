package com.ejrm.locationreminder.data

import com.ejrm.locationreminder.data.model.ReminderModel
import kotlinx.coroutines.flow.Flow

interface ReminderRepository {
    suspend fun addReminder(reminder: ReminderModel)
     fun getReminders(): Flow<List<ReminderModel>>
    suspend fun getReminderById(id: String): ReminderModel?
}
