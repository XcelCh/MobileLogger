package com.example.logging.worker

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority.PRIORITY_BALANCED_POWER_ACCURACY
import com.google.android.gms.tasks.CancellationToken
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.OnTokenCanceledListener
import java.util.Calendar
import com.example.logging.Location as Loc


class LocationWorker(private val ctx: Context, params: WorkerParameters) : CoroutineWorker(ctx, params){

    // declare a global variable of FusedLocationProviderClient
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override suspend fun doWork(): Result {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(ctx)

        getLastKnownLocation()

        return Result.success()
    }

    private fun getLastKnownLocation() {
        if (ActivityCompat.checkSelfPermission(
                ctx,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                ctx,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationClient.getCurrentLocation(PRIORITY_BALANCED_POWER_ACCURACY, object : CancellationToken() {
                override fun onCanceledRequested(p0: OnTokenCanceledListener) = CancellationTokenSource().token

                override fun isCancellationRequested() = false
            })
                .addOnSuccessListener { location: Location? ->
                    if (location == null) {
                        Log.e("Location", "Location is Null")
                        Toast.makeText(ctx, "Cannot get location.", Toast.LENGTH_SHORT).show()
                    } else {
                        val cal = Calendar.getInstance()
                        val date = cal.get(Calendar.DAY_OF_MONTH).toString() + "-" + cal.get(Calendar.MONTH).toString() + "-" + cal.get(Calendar.YEAR).toString()
                        val time = cal.get(Calendar.HOUR_OF_DAY).toString() + ":" + cal.get(Calendar.MINUTE).toString()

                        val loc = Loc(location.latitude.toString(), location.longitude.toString(), location.altitude.toString(),
                                        date, time)

                        Log.d("Location", "Latitude: ${loc.lat} \nLongitude: ${loc.lon} " +
                                "\nAltitude: ${loc.alt} \nDate: ${loc.date} \n Time: ${loc.time}")

                        val manager = WorkerManager()

                        manager.scheduleDatabaseWork(ctx, loc)
                    }
                }
        }
    }
}