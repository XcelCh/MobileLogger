package com.example.logging

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.logging.receiver.ContinueReceiver
import com.example.logging.receiver.StopReceiver

class Notification(private val message: String) {

    fun makeStopStatusNotification(context: Context) {
        val id = "stopNotification"

        // Make a channel if necessary
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create the NotificationChannel, but only on API 26+ because
            // the NotificationChannel class is new and not in the support library
            val name = "Logging Notification"
            val description = "Custom Background Running Logging Notification"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(id, name, importance)
            channel.description = description

            // Add the channel
            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?

            notificationManager?.createNotificationChannel(channel)
        }

        val stopIntent = Intent(context, StopReceiver::class.java).apply {
            action = "Stop"
        }

        val stopPendingIntent: PendingIntent =
            PendingIntent.getBroadcast(context, 0, stopIntent, PendingIntent.FLAG_IMMUTABLE)

        // Create the notification
        val builder = NotificationCompat.Builder(context, id)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Logging Started")
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .setOngoing(true)
            .addAction(
                androidx.appcompat.R.drawable.abc_btn_colored_material, "Stop",
                stopPendingIntent)
            .setVibrate(LongArray(0))

        // Show the notification
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        NotificationManagerCompat.from(context).notify(1, builder.build())
    }

    fun makeContinueStatusNotification(context: Context) {
        val id = "continueNotification"

        // Make a channel if necessary
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create the NotificationChannel, but only on API 26+ because
            // the NotificationChannel class is new and not in the support library
            val name = "Logging Notification"
            val description = "Custom Background Running Logging Notification"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(id, name, importance)
            channel.description = description

            // Add the channel
            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?

            notificationManager?.createNotificationChannel(channel)
        }

        val continueIntent = Intent(context, ContinueReceiver::class.java).apply {
            action = "Continue"
        }

        val continuePendingIntent: PendingIntent =
            PendingIntent.getBroadcast(context, 0, continueIntent, PendingIntent.FLAG_IMMUTABLE)

        // Create the notification
        val builder = NotificationCompat.Builder(context, id)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Logging Stopped")
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .setOngoing(true)
            .addAction(
                androidx.appcompat.R.drawable.abc_btn_colored_material, "Continue",
                continuePendingIntent)
            .setVibrate(LongArray(0))

        // Show the notification
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        NotificationManagerCompat.from(context).notify(2, builder.build())
    }
}