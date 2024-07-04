package com.ejrm.locationreminder.ui.screen

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.ejrm.locationreminder.data.model.ReminderModel
import com.ejrm.locationreminder.ui.viewmodel.ReminderViewModel
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker


@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun ReminderScreen(viewModel: ReminderViewModel = hiltViewModel()) {
    val reminders by viewModel.reminders.observeAsState(emptyList())
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    var reminderName by remember { mutableStateOf("") }
    var latitude by remember { mutableStateOf("") }
    var longitude by remember { mutableStateOf("") }
    var radius by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        ActivityCompat.requestPermissions(
            context as android.app.Activity,
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION
            ),
            1
        )
    }

    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    var currentLocation by remember { mutableStateOf<com.google.android.gms.maps.model.LatLng?>(null) }

    LaunchedEffect(Unit) {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return@LaunchedEffect
        }
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            currentLocation = com.google.android.gms.maps.model.LatLng(location.latitude, location.longitude)
        }
    }


    Column(modifier = Modifier.fillMaxSize()) {
        currentLocation?.let { location ->
            GoogleMap(
                modifier = Modifier.weight(1f),
                cameraPositionState = cameraPositionState
            ) {
                Marker(
                    state = MarkerState(position = location),
                    title = "Your Location"
                )
            }
        }

        Column(modifier = Modifier.weight(1f)) {
            TextField(
                value = reminderName,
                onValueChange = { reminderName = it },
                label = { Text("Reminder Name") }
            )
            TextField(
                value = latitude,
                onValueChange = { latitude = it },
                label = { Text("Latitude") }
            )
            TextField(
                value = longitude,
                onValueChange = { longitude = it },
                label = { Text("Longitude") }
            )
            TextField(
                value = radius,
                onValueChange = { radius = it },
                label = { Text("Radius") }
            )
            Button(onClick = {
                val reminder = ReminderModel(
                    id = reminderName.text,
                    name = reminderName.text,
                    latitude = latitude.text.toDouble(),
                    longitude = longitude.text.toDouble(),
                    radius = radius.text.toFloat()
                )
                viewModel.addReminder(reminder)
            }) {
                Text("Add Reminder")
            }
            LazyColumn {
                items(reminders) { reminder ->
                    Text(reminder.name)
                }
            }
        }
    }
}