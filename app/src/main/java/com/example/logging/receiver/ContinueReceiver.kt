package com.example.logging.receiver

import android.content.BroadcastReceiver
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationManagerCompat
import com.example.logging.Notification
import com.example.logging.worker.WorkerManager

class ContinueReceiver: BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
//        val startIntent = Intent(context, MainActivity::class.java).apply {
//            action = "Start"
//            flags = Intent.FLAG_ACTIVITY_NEW_TASK
//        }

        StringBuilder().apply {
            append("Action: ${intent.action}\n")
            append("URI: ${intent.toUri(Intent.URI_INTENT_SCHEME)}\n")
            toString().also { log ->
                Log.d(ContentValues.TAG, log)

                NotificationManagerCompat.from(context).cancel(2)
                val notify = Notification("Mobile Logger")
                notify.makeStopStatusNotification(context)

                val manager = WorkerManager()
                manager.scheduleLocationWork(context)
            }
        }
    }
}