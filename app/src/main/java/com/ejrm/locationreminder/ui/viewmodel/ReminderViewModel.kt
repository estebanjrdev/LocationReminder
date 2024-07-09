package com.ejrm.locationreminder.ui.viewmodel

import androidx.lifecycle.*
import com.ejrm.locationreminder.data.GeofencingManager
import com.ejrm.locationreminder.data.model.ReminderModel
import com.ejrm.locationreminder.domain.AddReminderUseCase
import com.ejrm.locationreminder.domain.GetRemindersUseCase

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReminderViewModel @Inject constructor(
    private val addReminderUseCase: AddReminderUseCase,
    private val getRemindersUseCase: GetRemindersUseCase,
    private val geofencingManager: GeofencingManager // Inyectar el GeofencingManager
) : ViewModel() {

    private val _reminders = MutableLiveData<List<ReminderModel>>()
    val reminders: LiveData<List<ReminderModel>> get() = _reminders

    init {
        viewModelScope.launch {
            getRemindersUseCase().collect { reminderList ->
                _reminders.postValue(reminderList)
            }
        }
    }

    fun addReminder(reminder: ReminderModel) {
        viewModelScope.launch {
            addReminderUseCase(reminder)
            geofencingManager.addGeofence(reminder) // Añadir geofence cuando se agrega un recordatorio
        }
    }
}

