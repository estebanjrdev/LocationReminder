package com.ejrm.locationreminder.ui.component

import com.ejrm.locationreminder.data.model.ReminderModel
import android.location.Location
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@Composable
fun DialogAddReminder(
    showDialog: Boolean,
    onDismissRequest: () -> Unit,
    onConfirmation: (ReminderModel) -> Unit,
    currentLocation: Location?
) {
    var nameReminder by remember { mutableStateOf("") }
    var radiusReminder by remember { mutableStateOf("") }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { onDismissRequest() },
            confirmButton = {
                Button(
                    onClick = {
                        if (currentLocation != null && nameReminder.isNotEmpty() && radiusReminder.isNotEmpty()) {
                            val reminder = ReminderModel(
                                id = nameReminder,
                                name = nameReminder,
                                latitude = currentLocation.latitude,
                                longitude = currentLocation.longitude,
                                radius = radiusReminder.toFloat()
                            )
                            onConfirmation(reminder)
                        }
                        onDismissRequest()
                    }
                ) {
                    Text("Agregar")
                }
            },
            dismissButton = {
                Button(
                    onClick = { onDismissRequest() }
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
                        label = { Text("Radio (metros)") },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Number
                        )
                    )
                }
            }
        )
    }
}
