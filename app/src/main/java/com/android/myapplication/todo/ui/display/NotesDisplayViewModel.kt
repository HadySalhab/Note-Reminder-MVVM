package com.android.myapplication.todo.ui.display

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.myapplication.todo.data.Notes
import com.android.myapplication.todo.repositories.NotesRepository
import com.android.myapplication.todo.util.Destination
import com.android.myapplication.todo.util.Event
import kotlinx.coroutines.launch

class NotesDisplayViewModel(private val notesRepository: NotesRepository,
                            private val noteIdentifier:String) : ViewModel(){
    private val _note = MutableLiveData<Notes>()
    val note:LiveData<Notes>
    get() = _note

    private val _navigationEvent = MutableLiveData< Event<Destination>>()
    val navigationEvent:LiveData<Event<Destination>>
    get() = _navigationEvent

    init {
        initializeNote()
    }
    fun initializeNote(){
        viewModelScope.launch {
            _note.value=notesRepository.getNoteById(noteIdentifier)
        }
    }

    fun navigateToEdit(){
        _navigationEvent.value = Event(Destination.EDITFRAGMENT)
    }
    fun navigateUp(){
        _navigationEvent.value = Event(Destination.LISTFRAGMENT)
    }

}