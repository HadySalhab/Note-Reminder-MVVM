package com.android.myapplication.todo.data.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.android.myapplication.todo.data.Notes

@Dao
interface NotesDao{

    /*Room automatically handle liveData on a background thread*/
    @Query("SELECT * FROM notes ORDER BY _id DESC")
    fun getNotes():LiveData<List<Notes>>

    @Query("SELECT * FROM notes WHERE favorite= 1 ORDER BY _id DESC")
    fun getFavoriteNotes():LiveData<List<Notes>>

    @Query("SELECT * FROM notes WHERE note_identifier = :noteId")
    suspend fun getNoteById(noteId:String):Notes?

    @Query("SELECT * FROM notes WHERE note_identifier = :noteId")
    fun getNoteLiveDataById(noteId:String):LiveData<Notes>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note:Notes)

    @Update
    suspend fun updateNote(note:Notes)

    @Delete
    suspend fun delete(note:Notes)

    @Query("DELETE FROM notes")
    suspend fun clearAllNotes()

}