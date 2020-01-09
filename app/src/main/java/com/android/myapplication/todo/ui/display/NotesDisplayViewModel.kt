package com.android.myapplication.todo.ui.display

import androidx.lifecycle.ViewModel
import com.android.myapplication.todo.repositories.NotesRepository

class NotesDisplayViewModel(private val notesRepository: NotesRepository,
                            private val noteId:Int) : ViewModel(){

}