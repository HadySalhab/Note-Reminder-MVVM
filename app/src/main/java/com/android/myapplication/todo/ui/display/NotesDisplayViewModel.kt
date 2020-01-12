package com.android.myapplication.todo.ui.display

import androidx.lifecycle.*
import com.android.myapplication.todo.data.Notes
import com.android.myapplication.todo.repositories.NotesRepository
import com.android.myapplication.todo.util.Destination
import com.android.myapplication.todo.util.Event
import kotlinx.coroutines.launch
import java.io.File

class NotesDisplayViewModel(
    private val notesRepository: NotesRepository,
    private val noteIdentifier: String
) : ViewModel() {

    val note: LiveData<Notes> = notesRepository.getNoteLiveDataById(noteIdentifier)
    val imageFile = Transformations.map(note){note->
        notesRepository.getPhotoFile(note)
    }


    private val _navigationEvent = MutableLiveData<Event<Destination>>()
    val navigationEvent: LiveData<Event<Destination>>
        get() = _navigationEvent


    fun navigateToEdit() {
        _navigationEvent.value = Event(Destination.EDITFRAGMENT)
    }

    fun navigateUp() {
        _navigationEvent.value = Event(Destination.UP)
    }

}