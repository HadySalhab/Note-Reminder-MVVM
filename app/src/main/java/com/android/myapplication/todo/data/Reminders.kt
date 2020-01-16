package com.android.myapplication.todo.data

import android.annotation.SuppressLint
import android.os.SystemClock
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.text.SimpleDateFormat
import java.util.*

@Entity(tableName = "reminders")
data class Reminders(
    @ColumnInfo(name = "_id")
    @PrimaryKey(autoGenerate = true)
    val id:Long =0L,
    @ColumnInfo(name="reminder_identifier")
    val reminderIndentifier:String = UUID.randomUUID().toString(),
    var title: String = "",
    @SuppressLint("SimpleDateFormat")
    var date: String = SimpleDateFormat("EEE, d MMM yyyy").format(Date()),
    var time: String = SimpleDateFormat("h:mm a").format(Date()),
    var repeat:Boolean = false,
    @ColumnInfo(name = "repeat_value")
    var repeatValue:Int = 0,
    @ColumnInfo(name = "repeat_unit")
    var repeatUnit:String="",
    @ColumnInfo(name = "active")
    var isActive:Boolean = false

    )