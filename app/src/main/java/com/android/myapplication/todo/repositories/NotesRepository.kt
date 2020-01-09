package com.android.myapplication.todo.repositories

import com.android.myapplication.todo.data.Notes
import com.android.myapplication.todo.data.db.NotesDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class NotesRepository (private val noteDao:NotesDao){

    fun getAllNotes()=noteDao.getNotes()
    fun getFavoriteNotes()=noteDao.getFavoriteNotes()

    suspend fun delete(note: Notes){
        withContext(Dispatchers.IO){
            noteDao.delete(note)
        }
    }
    suspend fun clear(){
        withContext(Dispatchers.IO){
            noteDao.clearAllNotes()
        }
    }

    suspend fun update(note:Notes){
        withContext(Dispatchers.IO){
            noteDao.updateNote(note)
        }
    }
    suspend fun insert(note:Notes){
        withContext(Dispatchers.IO){
            noteDao.insertNote(note)
        }
    }

    suspend fun getNoteById(noteId:Int){
        withContext(Dispatchers.IO){
            noteDao.getNoteById(noteId)
        }
    }



}