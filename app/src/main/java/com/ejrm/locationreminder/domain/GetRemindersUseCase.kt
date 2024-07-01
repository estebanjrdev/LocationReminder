package com.ejrm.locationreminder.domain


import com.ejrm.locationreminder.data.ReminderRepository
import com.ejrm.locationreminder.data.model.ReminderModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetRemindersUseCase @Inject constructor(
    private val repository: ReminderRepository
) {
     operator fun invoke(): Flow<List<ReminderModel>> {
        return repository.getReminders()
    }
}
