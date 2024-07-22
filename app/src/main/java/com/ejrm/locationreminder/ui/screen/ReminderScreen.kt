package com.ejrm.locationreminder.ui.screen


import android.annotation.SuppressLint
import android.location.Location
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ejrm.locationreminder.R
import com.ejrm.locationreminder.data.model.ReminderModel
import com.ejrm.locationreminder.ui.component.DialogAddReminder
import com.ejrm.locationreminder.ui.viewmodel.ReminderViewModel
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ReminderScreen(viewModel: ReminderViewModel = hiltViewModel()) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val currentLocation by viewModel.currentLocation.observeAsState(null)
    val mapUiSettings = remember { MapUiSettings(myLocationButtonEnabled = true) }
    val mapProperties = remember { MapProperties(isMyLocationEnabled = true) }
    val geofences by viewModel.geofences.observeAsState(emptyList())
    var longClickLatLng by remember { mutableStateOf<LatLng?>(null) }

    LaunchedEffect(Unit) {
        if (viewModel.hasLocationPermission()) {
            viewModel.fetchCurrentLocation()
        } else {
            Toast.makeText(context, "Permiso de ubicación no concedido", Toast.LENGTH_SHORT).show()
        }
    }
    var showDialog by remember { mutableStateOf(false) }
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    showDialog = true
                    Log.d("LOGLOCA", "CURRENT LOCATION: ${currentLocation?.latitude}, ${currentLocation?.longitude}")
                },
                modifier = Modifier.padding(16.dp),
                content = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_add_location),
                        contentDescription = "Agregar recordatorio"
                    )
                }
            )

        } ,
        floatingActionButtonPosition = FabPosition.End, // Posición del FAB en la esquina inferior derecha
        content = {
            Column(modifier = Modifier.fillMaxSize()) {
                currentLocation?.let { location ->
                    val cameraPositionState = rememberCameraPositionState {
                        position = CameraPosition.fromLatLngZoom(LatLng(location.latitude, location.longitude), 15f)
                    }
                    GoogleMap(
                        modifier = Modifier.weight(1f),
                        cameraPositionState = cameraPositionState,
                        uiSettings = mapUiSettings,
                        properties = mapProperties,
                        onMapLongClick = { latLng ->
                            longClickLatLng = latLng
                            showDialog = true
                        }

                    ){
                        currentLocation?.let {
                            Marker(
                                state = MarkerState(position = LatLng(it.latitude, it.longitude)),
                                title = "Current Location"
                            )
                        }
                        geofences.forEach { reminder ->
                            Marker(
                                state = MarkerState(position = LatLng(reminder.latitude, reminder.longitude)),
                                title = reminder.name
                            )
                            Circle(
                                center = LatLng(reminder.latitude, reminder.longitude),
                                radius = reminder.radius.toDouble(),
                                strokeColor = MaterialTheme.colorScheme.primary,
                                strokeWidth = 2f,
                                fillColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
                            )
                        }
                    }
                }
            }
        }
    )
    DialogAddReminder(
        showDialog = showDialog,
        onDismissRequest = { showDialog = false },
        onConfirmation = { reminder ->
            longClickLatLng?.let { latLng ->
                val updatedReminder = reminder.copy(
                    latitude = latLng.latitude,
                    longitude = latLng.longitude
                )
                viewModel.addReminder(updatedReminder)
            }
        },
        currentLocation = longClickLatLng?.let {
            Location("").apply {
                latitude = it.latitude
                longitude = it.longitude
            }
        }
    )
}


