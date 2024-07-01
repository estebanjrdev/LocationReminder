package com.ejrm.locationreminder.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.hilt.navigation.compose.hiltViewModel
import com.ejrm.locationreminder.data.model.ReminderModel
import com.ejrm.locationreminder.ui.viewmodel.ReminderViewModel


@Composable
fun ReminderScreen(viewModel: ReminderViewModel = hiltViewModel()) {
    val reminders by viewModel.reminders.observeAsState(emptyList())

    var reminderName by remember { mutableStateOf("") }
    var latitude by remember { mutableStateOf("") }
    var longitude by remember { mutableStateOf("") }
    var radius by remember { mutableStateOf("") }

    Column {
        TextField(value = reminderName, onValueChange = { reminderName = it }, label = { Text("Reminder Name") })
        TextField(value = latitude, onValueChange = { latitude = it }, label = { Text("Latitude") })
        TextField(value = longitude, onValueChange = { longitude = it }, label = { Text("Longitude") })
        TextField(value = radius, onValueChange = { radius = it }, label = { Text("Radius") })

        Button(onClick = {
            val reminder = ReminderModel(
                id = reminderName,
                name = reminderName,
                latitude = latitude.toDouble(),
                longitude = longitude.toDouble(),
                radius = radius.toFloat()
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
