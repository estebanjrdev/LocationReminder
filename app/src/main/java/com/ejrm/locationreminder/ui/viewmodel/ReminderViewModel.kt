package com.ejrm.locationreminder.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ejrm.locationreminder.data.model.ReminderModel
import com.ejrm.locationreminder.domain.AddReminderUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReminderViewModel @Inject constructor(
    private val addReminderUseCase: AddReminderUseCase,
    // Otros casos de uso necesarios
) : ViewModel() {

    private val _reminders = MutableLiveData<List<ReminderModel>>()
    val reminders: LiveData<List<ReminderModel>> get() = _reminders

    fun addReminder(reminder: ReminderModel) {
        viewModelScope.launch {
            addReminderUseCase(reminder)
            // Actualizar lista de recordatorios
        }
    }
}
