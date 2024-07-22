package com.ejrm.locationreminder.ui.viewmodel

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import androidx.core.content.ContextCompat
import androidx.lifecycle.*
import com.ejrm.locationreminder.data.GeofencingManager
import com.ejrm.locationreminder.data.model.ReminderModel
import com.ejrm.locationreminder.domain.AddReminderUseCase
import com.ejrm.locationreminder.domain.GetRemindersUseCase
import com.ejrm.locationreminder.utils.LocationProvider
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.internal.Contexts.getApplication

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReminderViewModel @Inject constructor(
    private val addReminderUseCase: AddReminderUseCase,
    private val getRemindersUseCase: GetRemindersUseCase,
    private val geofencingManager: GeofencingManager,
    private val locationProvider: LocationProvider,
    private val context: Context
) : ViewModel() {

    private val _reminders = MutableLiveData<List<ReminderModel>>()
    val reminders: LiveData<List<ReminderModel>> get() = _reminders

    private val _currentLocation = MutableLiveData<Location?>()
    val currentLocation: MutableLiveData<Location?> get() = _currentLocation

    private val _geofences = MutableLiveData<List<ReminderModel>>()
    val geofences: LiveData<List<ReminderModel>> get() = _geofences

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
            geofencingManager.addGeofence(reminder) //

            val currentGeofences = _geofences.value.orEmpty().toMutableList()
            currentGeofences.add(reminder)
            _geofences.postValue(currentGeofences)
        }
    }

    fun fetchCurrentLocation() {
        viewModelScope.launch {
            val location = locationProvider.getUserLocation(context)
            _currentLocation.postValue(location)
        }
    }

    fun hasLocationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }


    fun createReminderFromMap(longClickLatLng: LatLng, name: String, radius: Float) {
        val reminder = ReminderModel(
            id = name + longClickLatLng.toString(),
            name = name,
            latitude = longClickLatLng.latitude,
            longitude = longClickLatLng.longitude,
            radius = radius
        )
        addReminder(reminder)
    }
}

