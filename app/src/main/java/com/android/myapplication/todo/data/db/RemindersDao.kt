package com.android.myapplication.todo.data.db


import androidx.lifecycle.LiveData
import androidx.room.*
import com.android.myapplication.todo.data.Notes
import com.android.myapplication.todo.data.Reminders

@Dao
interface RemindersDao{

    /*Room automatically handle liveData on a background thread*/
    @Query("SELECT * FROM reminders ORDER BY _id DESC")
    fun getReminders():LiveData<List<Reminders>>

    @Query("SELECT * FROM reminders ORDER BY _id DESC")
    fun getRemindersList():List<Reminders>?

    @Query("SELECT * FROM reminders WHERE reminder_identifier = :reminderId")
    suspend fun getReminderById(reminderId:String):Reminders?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReminder(reminder:Reminders)

    @Update
    suspend fun updateReminder(reminder:Reminders)

    @Delete
    suspend fun delete(reminder: Reminders)

    @Query("DELETE FROM reminders")
    suspend fun clearAllreminders()

    @Query("SELECT * FROM reminders ORDER BY _id DESC LIMIT 1")
    suspend fun getLatestReminder():Reminders?

}