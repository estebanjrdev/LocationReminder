package com.ejrm.locationreminder.domain

import com.ejrm.locationreminder.data.ReminderRepository
import com.ejrm.locationreminder.data.model.ReminderModel
import javax.inject.Inject

class AddReminderUseCase @Inject constructor(
    private val repository: ReminderRepository
) {
    suspend operator fun invoke(reminder: ReminderModel) {
        repository.addReminder(reminder)
    }
}
