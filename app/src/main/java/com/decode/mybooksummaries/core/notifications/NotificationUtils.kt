package com.decode.mybooksummaries.core.notifications

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.decode.mybooksummaries.R
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.TaskStackBuilder
import androidx.core.content.ContextCompat
import com.decode.mybooksummaries.MainActivity
import androidx.core.net.toUri

object NotificationUtils {

    private const val CHANNEL_ID = "reading_reminder_channel"
    private const val CHANNEL_NAME = "Okuma Hatırlatmaları"

    fun sendNotification(
        context: Context,
        title: String,
        message: String,
        navigateToEditProfile: Boolean = false
    ) {
        if (!hasNotificationPermission(context)) {
            Log.w("NotificationUtils", "Bildirim izni verilmemiş.")
            return
        }

        val notification = buildNotification(context, title, message, navigateToEditProfile)
        val notificationId = System.currentTimeMillis().toInt()

        try {
            NotificationManagerCompat.from(context).notify(notificationId, notification)
        } catch (e: SecurityException) {
            Log.e("NotificationUtils", "Bildirim gönderilemedi: Bildirim izni yok.", e)
        }
    }

    private fun hasNotificationPermission(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            true
        }
    }

    fun createNotificationChannel(context: Context) {
        val channel = NotificationChannel(
            CHANNEL_ID,
            CHANNEL_NAME,
            NotificationManager.IMPORTANCE_DEFAULT
        )

        val notificationManager = context.getSystemService(NotificationManager::class.java)
        notificationManager?.createNotificationChannel(channel)
    }

    private fun buildNotification(
        context: Context,
        title: String,
        message: String,
        navigateToEditProfile: Boolean = false
    ): Notification {
        val intent = if (navigateToEditProfile) {
            Intent(
                context, MainActivity::class.java
            ).apply {
                data = "myapp://edit_profile".toUri()
            }
        } else {
            Intent(context, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
        }
        val pendingIntent = TaskStackBuilder.create(context).run {
            addNextIntentWithParentStack(intent)
            getPendingIntent(0, PendingIntent.FLAG_IMMUTABLE)
        }

        return NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(title)
            .setContentText(message)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .build()
    }
}