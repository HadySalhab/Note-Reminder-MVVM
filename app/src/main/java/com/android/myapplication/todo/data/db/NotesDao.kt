package com.android.myapplication.todo.data.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.android.myapplication.todo.data.Notes

@Dao
interface NotesDao{

    /*Room automatically handle liveData on a background thread*/
    @Query("SELECT * FROM notes ORDER BY id DESC")
    fun getNotes():LiveData<Notes>

    @Query("SELECT * FROM notes WHERE favorite= 1 ORDER BY id DESC")
    fun getFavoriteNotes():LiveData<Notes>

    @Query("SELECT * FROM notes WHERE id = :noteId")
    suspend fun getNoteById(noteId:Int):Notes?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note:Notes)

    @Update
    suspend fun updateNote(note:Notes)

    @Delete
    suspend fun delete(note:Notes)

    @Query("DELETE FROM notes")
    suspend fun clearAllNotes()

}