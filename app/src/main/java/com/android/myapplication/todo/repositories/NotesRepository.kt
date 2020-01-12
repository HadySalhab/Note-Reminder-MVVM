package com.android.myapplication.todo.repositories

import android.content.Context
import com.android.myapplication.todo.data.Notes
import com.android.myapplication.todo.data.db.NotesDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

class NotesRepository(private val noteDao: NotesDao, private val context: Context) {

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

}