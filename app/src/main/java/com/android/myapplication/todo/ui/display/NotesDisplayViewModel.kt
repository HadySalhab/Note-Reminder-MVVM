package com.android.myapplication.todo.ui.display

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.myapplication.todo.data.Notes
import com.android.myapplication.todo.repositories.NotesRepository
import kotlinx.coroutines.launch

class NotesDisplayViewModel(private val notesRepository: NotesRepository,
                            private val noteIdentifier:String) : ViewModel(){
    private val _note = MutableLiveData<Notes>()
    val note:LiveData<Notes>
    get() = _note
    init {
        initializeNote()
    }
    fun initializeNote(){
        viewModelScope.launch {
            _note.value=notesRepository.getNoteById(noteIdentifier)
        }
    }

}