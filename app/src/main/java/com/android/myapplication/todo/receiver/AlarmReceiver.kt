package com.android.myapplication.todo.receiver

import android.app.NotificationManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.android.myapplication.todo.MainActivity
import com.android.myapplication.todo.R
import com.android.myapplication.todo.util.INTENT_EXTRA_REMINDER_IDENTIFIER
import com.android.myapplication.todo.util.INTENT_EXTRA_ROW_ID
import com.android.myapplication.todo.util.INTENT_EXTRA_TITLE

class AlarmReceiver:BroadcastReceiver(){
    override fun onReceive(context: Context?, intent: Intent?) {
        //notification Extras..
        val notificationId = intent?.extras?.getInt(INTENT_EXTRA_ROW_ID)?:0
        val reminderIdentifier = intent?.extras?.getString(INTENT_EXTRA_REMINDER_IDENTIFIER)
        val title = intent?.extras?.getString(INTENT_EXTRA_TITLE)

        val notificationIntent = Intent(context,MainActivity::class.java)
        notificationIntent.putExtra(INTENT_EXTRA_REMINDER_IDENTIFIER,reminderIdentifier)
        val pendingIntent = PendingIntent.getActivity(context,notificationId,notificationIntent,FLAG_UPDATE_CURRENT)

        val notificationBuilder = NotificationCompat.Builder(context!!,context.getString(R.string.reminder_notification_channel_id))
            .setLargeIcon(BitmapFactory.decodeResource(context.resources,R.mipmap.ic_launcher))
            .setSmallIcon(R.drawable.ic_notifications_smallicon)
            .setContentTitle(title)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setOnlyAlertOnce(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)

        val notificationManager = ContextCompat.getSystemService(context,NotificationManager::class.java) as NotificationManager
        notificationManager.notify(notificationId,notificationBuilder.build())

    }

    companion object {
        fun getAlarmPendingIntent(
            pckgContext: Context,
            notificationId: Int,
            reminderId: String,
            reminderTitle: String
        ): PendingIntent {
            val intent = Intent(pckgContext, AlarmReceiver::class.java)
            intent.apply {
                putExtra(INTENT_EXTRA_REMINDER_IDENTIFIER, reminderId)
                putExtra(INTENT_EXTRA_ROW_ID, notificationId)
                putExtra(INTENT_EXTRA_TITLE, reminderTitle)
            }
            val pendingIntent = PendingIntent.getBroadcast(
                pckgContext,
                notificationId,
                intent,
                PendingIntent.FLAG_CANCEL_CURRENT
            )
            return pendingIntent
        }
    }

}