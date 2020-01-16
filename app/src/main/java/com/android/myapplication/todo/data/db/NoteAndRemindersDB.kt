package com.android.myapplication.todo.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.android.myapplication.todo.data.Notes
import com.android.myapplication.todo.data.Reminders

private const val NOTES_DB_NAME = "notes.db"


@Database(entities = [Notes::class,Reminders::class], version = 1, exportSchema = false)
abstract class NoteAndRemindersDB : RoomDatabase() {
    abstract val notesDao: NotesDao
    abstract val remindersDao:RemindersDao

    companion object {
        fun getInstance(context: Context): NoteAndRemindersDB = Room.databaseBuilder(
            context.applicationContext,
            NoteAndRemindersDB::class.java, NOTES_DB_NAME
        ).fallbackToDestructiveMigration().build()
    }
}