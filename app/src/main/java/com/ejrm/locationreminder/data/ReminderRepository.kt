package com.ejrm.locationreminder.data

import com.ejrm.locationreminder.data.model.ReminderModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ReminderRepository @Inject constructor(
    // Dependencias necesarias, como bases de datos o servicios remotos
) : ReminderProvider {

    override suspend fun addReminder(reminder: ReminderModel) {
        TODO("Not yet implemented")
    }

    override suspend fun getReminders(): Flow<List<ReminderModel>> {
        TODO("Not yet implemented")
    }

    override suspend fun getReminderById(id: String): ReminderModel? {
        TODO("Not yet implemented")
    }
}