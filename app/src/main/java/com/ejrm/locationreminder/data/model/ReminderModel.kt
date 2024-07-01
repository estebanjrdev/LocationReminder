package com.ejrm.locationreminder.data.model

import java.util.Date

data class ReminderModel(
    val id: String,
    val name: String,
    val latitude: Double,
    val longitude: Double,
    val radius: Float,
    val activationDate: Date? = null
)