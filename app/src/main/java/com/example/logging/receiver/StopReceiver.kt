package com.example.logging.receiver

import android.content.BroadcastReceiver
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationManagerCompat
import com.example.logging.Notification
import kotlin.system.exitProcess


class StopReceiver: BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        StringBuilder().apply {
            append("Action: ${intent.action}\n")
            append("URI: ${intent.toUri(Intent.URI_INTENT_SCHEME)}\n")
            toString().also { log ->
                Log.d(TAG, log)

                NotificationManagerCompat.from(context).cancel(1)
                val notify = Notification("Mobile Logger")
                notify.makeContinueStatusNotification(context)

                exitProcess(0)
            }
        }
    }
}