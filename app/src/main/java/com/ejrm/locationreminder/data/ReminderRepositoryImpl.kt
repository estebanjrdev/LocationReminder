package com.ejrm.locationreminder.data

import com.ejrm.locationreminder.data.local.ReminderDao
import com.ejrm.locationreminder.data.local.ReminderEntity
import com.ejrm.locationreminder.data.model.ReminderModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ReminderRepositoryImpl @Inject constructor(
    private val reminderDao: ReminderDao
) : ReminderRepository {

    override suspend fun addReminder(reminder: ReminderModel) {
        reminderDao.insertReminder(reminder.toEntity())
    }

    override fun getReminders(): Flow<List<ReminderModel>> {
        return reminderDao.getReminders().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun getReminderById(id: String): ReminderModel? {
        return reminderDao.getReminderById(id)?.toDomain()
    }

    // Convert domain model to entity and vice versa
    private fun ReminderModel.toEntity() = ReminderEntity(id, name, latitude, longitude, radius, activationDate)
    private fun ReminderEntity.toDomain() = ReminderModel(id, name, latitude, longitude, radius, activationDate)
}