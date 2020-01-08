package com.android.myapplication.todo.data.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.android.myapplication.todo.data.Notes

@Dao
interface NotesDao{

    /*Room automatically handle liveData on a background thread*/
    @Query("SELECT * FROM notes")
    fun getNotes():LiveData<Notes>

    @Query("SELECT * FROM notes WHERE noteId = :noteId")
    suspend fun getNoteById(noteId:String):Notes?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note:Notes)

    @Update
    suspend fun updateNote(note:Notes)

    @Query("DELETE FROM notes WHERE noteId = :noteId")
    suspend fun deleteNoteById(noteId: String)

    @Query("DELETE FROM notes")
    suspend fun clearAllNotes()

    /*In Sqlite, 1 means true and 0 means false*/
    @Query("DELETE FROM notes WHERE completed=1")
    suspend fun deleteCompletedNotes()

}