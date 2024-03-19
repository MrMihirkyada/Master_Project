package com.example.masterproject

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Color
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class CustomNotification(private val context: Context, var place: String, var notes: String) {
    private val notificationManager = NotificationManagerCompat.from(context)

    @SuppressLint("MissingPermission")
    fun show() {
        val contentView = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.logo) // Use your own icon here
            .setContentTitle(place)
            .setContentText(notes)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setStyle(NotificationCompat.DecoratedCustomViewStyle())
            .build()
        notificationManager.notify(NOTIFICATION_ID, contentView)
    }

    companion object
    {
        private const val CHANNEL_ID = "custom_notification_channel"
        private const val CHANNEL_NAME = "Custom Notification Channel"
        private const val NOTIFICATION_ID = 1
    }

    init
    {
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance).apply {
                description = "Custom Notification Channel"
                enableLights(true)
                lightColor = Color.RED
                enableVibration(true)
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    @SuppressLint("MissingPermission")
    fun sendNotificationToAllUsers(packageName: String) {
        val notificationContent = "New package added: $packageName"
        // Assuming you have a method to get all user tokens/devices from your backend
        val userTokens = getAllUserTokensFromBackend()

        userTokens.forEach { userToken ->
            val notificationBuilder = NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.logo)
                .setContentTitle("New Package Notification")
                .setContentText(notificationContent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setStyle(NotificationCompat.DecoratedCustomViewStyle())

            notificationManager.notify(userToken.hashCode(), notificationBuilder.build())
        }
    }

    private fun getAllUserTokensFromBackend(): List<String> {
        // This method should retrieve all user tokens/devices from your backend
        // Replace this with your actual implementation
        return listOf("userToken1", "userToken2", "userToken3") // Example list of user tokens
    }
}