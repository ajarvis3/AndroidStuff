package com.example.notificationapp

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import java.util.*

// based on https://developer.android.com/training/notify-user/build-notification
// trying to make back button go back to main activity simply does not work, even after
// following steps in https://developer.android.com/training/notify-user/navigation
class MainActivity : AppCompatActivity() {
    val CHANNEL_ID =  UUID.randomUUID().toString() // NotificationChannel.DEFAULT_CHANNEL_ID

    fun myNotification() {
        // Create an explicit intent for an Activity in your app
        val intent = Intent(this, MainActivity2::class.java).apply {
            // this means it doesn't have the normal back and up button experience...
            // first flag makes a new task
            // second makes it so that it's the root of tasks, other activities are finished
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent, 0)
        val notificationId = UUID.randomUUID().hashCode()

        var builder = NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_baseline_coronavirus_24)
                .setContentTitle("My New Notification")
                .setContentText("This is much longer text and I will just keep typing for now")
                .setStyle(NotificationCompat.BigTextStyle()
                        .bigText("This is much longer text and I will just keep typing for now"))
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                // clears notification on tap
                .setAutoCancel(true)

        with(NotificationManagerCompat.from(this)) {
            // notificationId is a unique int for each notification that you must define
            notify(notificationId, builder.build())
        }
    }

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.channel_name)
            val descriptionText = getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                    getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        createNotificationChannel()
        val btn = findViewById<Button>(R.id.notificationButton)
        btn.setOnClickListener {
            myNotification()
        }
    }
}