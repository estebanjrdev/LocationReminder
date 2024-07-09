package com.ejrm.locationreminder.data

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import com.ejrm.locationreminder.data.model.ReminderModel
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingClient
import com.google.android.gms.location.GeofencingRequest
import com.google.android.gms.location.LocationServices
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GeofencingManager @Inject constructor(
    private val context: Context
) {

    private val geofencingClient: GeofencingClient = LocationServices.getGeofencingClient(context)


    fun addGeofence(reminder: ReminderModel) {
        if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_BACKGROUND_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Handle the case where permissions are not granted
            return
        }
        val geofence = Geofence.Builder()
            .setRequestId(reminder.id)
            .setCircularRegion(
                reminder.latitude,
                reminder.longitude,
                reminder.radius
            )
            .setExpirationDuration(Geofence.NEVER_EXPIRE)
            .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER)
            .build()

        val geofencingRequest = GeofencingRequest.Builder()
            .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
            .addGeofence(geofence)
            .build()

        val geofencePendingIntent: PendingIntent by lazy {
            val intent = Intent(context, GeofenceBroadcastReceiver::class.java)
            PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
        }

        geofencingClient.addGeofences(geofencingRequest, geofencePendingIntent).run {
            addOnSuccessListener {
                // Geofence added
                println("Geofence added successfully: ${reminder.id}")
            }
            addOnFailureListener {e->
                // Failed to add geofence
                println("Failed to add geofence: ${reminder.id}")
                e.printStackTrace()
            }
        }
    }

    fun removeGeofence(reminder: ReminderModel) {
        if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_BACKGROUND_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Handle the case where permissions are not granted
            return
        }
        geofencingClient.removeGeofences(listOf(reminder.id)).run {
            addOnSuccessListener {
                // Geofence removed
                println("Geofence removed successfully: ${reminder.id}")
            }
            addOnFailureListener { e ->
                // Failed to remove geofence
                println("Failed to remove geofence: ${reminder.id}")
                e.printStackTrace()
            }
        }
    }
}
