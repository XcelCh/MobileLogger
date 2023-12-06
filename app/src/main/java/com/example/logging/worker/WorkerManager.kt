package com.example.logging.worker

import android.content.Context
import androidx.work.Data
import androidx.work.OneTimeWorkRequest
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.example.logging.Location
import java.util.concurrent.TimeUnit

class WorkerManager {

    fun scheduleLocationWork(context: Context) {
        val periodicWorkRequest =
            PeriodicWorkRequest.Builder(LocationWorker::class.java, 15, TimeUnit.MINUTES)
                .build()

        WorkManager.getInstance(context).enqueue(periodicWorkRequest)
    }

    fun scheduleDatabaseWork(context: Context, location: Location){
        val oneTimeRequest = OneTimeWorkRequest.Builder(DatabaseWorker::class.java)

        val data = Data.Builder()

        data.putString("Longitude", location.lon)
        data.putString("Latitude", location.lat)
        data.putString("Altitude", location.alt)
        data.putString("Date", location.date)
        data.putString("Time", location.time)

        oneTimeRequest.setInputData(data.build())

        WorkManager.getInstance(context).enqueue(oneTimeRequest.build())
    }
}