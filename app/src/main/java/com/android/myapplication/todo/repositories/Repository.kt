package com.android.myapplication.todo.repositories

import android.content.Context
import com.android.myapplication.todo.data.Notes
import com.android.myapplication.todo.data.Reminders
import com.android.myapplication.todo.data.db.NotesDao
import com.android.myapplication.todo.data.db.RemindersDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

class Repository(
    private val noteDao: NotesDao,
    private val reminderDao: RemindersDao,
    private val context: Context
) {

    private val filesDir = context.applicationContext.filesDir

    fun getAllNotes() = noteDao.getNotes()
    fun getFavoriteNotes() = noteDao.getFavoriteNotes()
    fun getNoteLiveDataById(noteId: String) = noteDao.getNoteLiveDataById(noteId)

    suspend fun delete(note: Notes) {
        withContext(Dispatchers.IO) {
            noteDao.delete(note)
        }
    }

    suspend fun clear() {
        withContext(Dispatchers.IO) {
            noteDao.clearAllNotes()
        }
    }

    suspend fun update(note: Notes) {
        withContext(Dispatchers.IO) {
            noteDao.updateNote(note)
        }
    }

    suspend fun insert(note: Notes) {
        withContext(Dispatchers.IO) {
            noteDao.insertNote(note)
        }
    }

    suspend fun getNoteById(noteId: String): Notes? {
        return withContext(Dispatchers.IO) {
            noteDao.getNoteById(noteId)
        }
    }

    fun getPhotoFile(note: Notes): File = File(filesDir, note.photoFileName)




    fun getAllReminders() = reminderDao.getReminders()

    suspend fun getRemindersList():List<Reminders>?{
        return withContext(Dispatchers.IO){
            reminderDao.getRemindersList()
        }
    }

    suspend fun getReminderById(reminderId: String): Reminders? {
        return withContext(Dispatchers.IO) {
            reminderDao.getReminderById(reminderId)
        }
    }

    suspend fun getLatestReminder():Reminders?{
        return  withContext(Dispatchers.IO){
            reminderDao.getLatestReminder()
        }
    }

    suspend fun insert(reminder: Reminders){
        withContext(Dispatchers.IO) {
            reminderDao.insertReminder(reminder)
        }
    }

    suspend fun update(reminder: Reminders) {
        withContext(Dispatchers.IO) {
            reminderDao.updateReminder(reminder)
        }
    }
    suspend fun delete(reminder: Reminders) {
        withContext(Dispatchers.IO) {
            reminderDao.delete(reminder)
        }
    }
}