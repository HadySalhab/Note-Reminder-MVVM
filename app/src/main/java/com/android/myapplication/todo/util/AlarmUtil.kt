package com.android.myapplication.todo.util

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.os.SystemClock
import android.util.Log
import androidx.core.app.AlarmManagerCompat
import com.android.myapplication.todo.data.Reminders
import com.android.myapplication.todo.receiver.AlarmReceiver
import java.text.SimpleDateFormat
import java.util.*

class AlarmUtil {
    companion object {
            private const val TAG = "AlarmUtil"

        private fun getTriggerTime(dateString: String, timeString: String): Long {
            val date = SimpleDateFormat("EEE, d MMM yyyy").parse(dateString)
            val time = SimpleDateFormat("h:mm a").parse(timeString)
            val calendar = Calendar.getInstance()
            calendar.time = date ?: Date()
            val initialYear = calendar.get(Calendar.YEAR)
            val initialMonth = calendar.get(Calendar.MONTH)
            val initialDate = calendar.get(Calendar.DAY_OF_MONTH)
            calendar.time = time ?: Date()
            val initialHourOfDay = calendar.get(Calendar.HOUR_OF_DAY)
            val initialMinute = calendar.get(Calendar.MINUTE)
            calendar.set(initialYear, initialMonth, initialDate, initialHourOfDay, initialMinute)
            val timeDiff = calendar.timeInMillis - Calendar.getInstance().timeInMillis
            return SystemClock.elapsedRealtime() + timeDiff
        }

        private fun getRepeatTime(value: Int, unit: String): Long {
            return when (unit) {
                "Minute" ->
                    value * millisMinute
                "Hour" ->
                    value * millisHour
                "Day" ->
                    value * millisDay
                "Week" ->
                    value * millisWeek
                "Month" ->
                    value * millisMonth
                else ->
                    value * millisDay
            }
        }

        fun createAlarm(
            context: Context,
            reminder: Reminders,
            alarmManager: AlarmManager
        ) {
            val alarmPendingIntent = AlarmReceiver.getAlarmPendingIntent(
                context,
                reminder.id,
                reminder.reminderIndentifier,
                reminder.title
            )

            if (reminder.repeat) {
                alarmManager.setRepeating(
                    AlarmManager.ELAPSED_REALTIME_WAKEUP,
                    getTriggerTime(reminder.date, reminder.time),
                    getRepeatTime(reminder.repeatValue, reminder.repeatUnit),
                    alarmPendingIntent
                )
            } else {
                AlarmManagerCompat.setExactAndAllowWhileIdle(
                    alarmManager,
                    AlarmManager.ELAPSED_REALTIME_WAKEUP,
                    getTriggerTime(reminder.date, reminder.time),
                    alarmPendingIntent
                )
            }
        }

        fun cancelAlarm(
            context:Context,
            reminder: Reminders,
            alarmManager: AlarmManager
        ) {
            val alarmPendingIntent = AlarmReceiver.getAlarmPendingIntent(
                context,
                reminder.id,
                reminder.reminderIndentifier,
                reminder.title
            )
            Log.d(TAG, "cancelAlarm: ")

            alarmManager.cancel(alarmPendingIntent)
        }
    }
}