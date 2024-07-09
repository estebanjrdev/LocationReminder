package com.ejrm.locationreminder.ui.screen


import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.ejrm.locationreminder.R
import com.ejrm.locationreminder.data.model.ReminderModel
import com.ejrm.locationreminder.ui.viewmodel.ReminderViewModel
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ReminderScreen(viewModel: ReminderViewModel = hiltViewModel()) {
    val reminders by viewModel.reminders.observeAsState(emptyList())
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    var currentLocation by remember { mutableStateOf<LatLng?>(null) }

    fun requestLocation() {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                currentLocation = LatLng(location.latitude, location.longitude)
            }.addOnFailureListener { e ->
                Toast.makeText(context, "No se pudo obtener la ubicación", Toast.LENGTH_SHORT).show()
                e.printStackTrace()
            }
        } else {
            Toast.makeText(context, "Permiso de ubicación no concedido", Toast.LENGTH_SHORT).show()
        }
    }
    LaunchedEffect(Unit) {
        requestLocation()
    }
    var showDialog by remember { mutableStateOf(false) }
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    showDialog = true
                },
                modifier = Modifier.padding(16.dp),
                content = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_my_location),
                        contentDescription = "Add Location"
                    )
                }
            )

        } ,
        floatingActionButtonPosition = FabPosition.End, // Posición del FAB en la esquina inferior derecha
        content = {
            Column(modifier = Modifier.fillMaxSize()) {
                currentLocation?.let { location ->
                    val cameraPositionState = rememberCameraPositionState {
                        position = CameraPosition.fromLatLngZoom(location, 15f)
                    }

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
            }
        }
    )
    DialogAddReminder(showDialog,viewModel, currentLocation)
}

@Composable
fun DialogAddReminder(showDialog: Boolean, viewModel: ReminderViewModel,currentLocation: LatLng?) {
    var nameReminder by remember { mutableStateOf("") }
    var radiusReminder by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        if (showDialog) {
            AlertDialog(
                onDismissRequest = { },
                confirmButton = {
                    Button(
                        onClick = {
                            val reminder = currentLocation?.let {
                                ReminderModel(
                                    id = nameReminder,
                                    name = nameReminder,
                                    latitude = it.latitude,
                                    longitude = currentLocation.longitude,
                                    radius = radiusReminder.toFloat()
                                )
                            }
                            if (reminder != null) {
                                viewModel.addReminder(reminder)
                            }
                        }
                    ) {
                        Text("Agregar")
                    }
                },
                dismissButton = {
                    Button(
                        onClick = {  }
                    ) {
                        Text("Cerrar")
                    }
                },
                title = { Text("Agregar Recordatorio") },
                text = {
                    Column {
                        TextField(
                            value = nameReminder,
                            onValueChange = { nameReminder = it },
                            label = { Text("Nombre") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        TextField(
                            value = radiusReminder,
                            onValueChange = { radiusReminder = it },
                            label = { Text("Radio") },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            )
        }
    }
}
