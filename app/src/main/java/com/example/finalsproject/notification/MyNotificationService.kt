package com.example.finalsproject.notification

import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import com.example.finalsproject.R

class MyNotificationService(private val context: Context) {

    private val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    fun showNotification() {
        val notification = NotificationCompat.Builder(context, NOTIFICATION_ID)
            .setSmallIcon(R.drawable.ic_baseline_like_icon)
            .setContentTitle("Congratulations!")
            .setContentText("You have finished a task. Be proud of your productivity <3")
            .setAutoCancel(true)
            .build()

        notificationManager.notify(1, notification)
    }


    companion object {
        const val NOTIFICATION_ID = "congrats_channel"
    }
}