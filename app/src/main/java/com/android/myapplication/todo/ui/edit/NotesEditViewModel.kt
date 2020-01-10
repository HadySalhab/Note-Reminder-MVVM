package com.android.myapplication.todo.ui.edit

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.android.myapplication.todo.data.Notes
import com.android.myapplication.todo.repositories.NotesRepository
import com.android.myapplication.todo.util.Event
import kotlinx.coroutines.launch
import com.android.myapplication.todo.R

class NotesEditViewModel(
    private val notesRepository: NotesRepository,
    private val noteIdentifier: String?,
    private val app: Application
) : AndroidViewModel(app) {


    companion object {
        private const val TAG = "NotesEditViewModel"
    }

    private lateinit var editableNote: Notes

    init {
        initializeNote()
    }

    private val _snackBarEvent = MutableLiveData<Event<String>>()
    val snackBarEvent: LiveData<Event<String>>
        get() = _snackBarEvent

    private val _navigateUpEvent=MutableLiveData<Event<Unit>>()
    val navigateUpEvent:LiveData<Event<Unit>>
    get() = _navigateUpEvent


    val titleEditText = MutableLiveData<String>()

    val descriptionEditText = MutableLiveData<String>()

    val favoriteCheckBox = MutableLiveData<Boolean>()

    val dateTextView = MutableLiveData<String>()


    fun initializeNote() {
        viewModelScope.launch {
            if(noteIdentifier!=null){
                editableNote=notesRepository.getNoteById(noteIdentifier)?:Notes()
            }else{
                editableNote=Notes()
            }
            updateUI()
        }
    }

    fun updateUI() {
        titleEditText.value = editableNote.title
        descriptionEditText.value = editableNote.description
        favoriteCheckBox.value = editableNote.isFavorite
        dateTextView.value = editableNote.date
    }

    override fun onCleared() {
        super.onCleared()
        showlog()
    }

    fun showlog() {
        Log.d(TAG, "showlog: title is ${titleEditText.value}")
        Log.d(TAG, "showlog: desc is ${descriptionEditText.value}")
        Log.d(TAG, "showlog: isfav ${favoriteCheckBox.value}")
        Log.d(TAG, "showlog: date is ${dateTextView.value}")
    }


    fun deleteNote(){
        viewModelScope.launch {
            notesRepository.delete(editableNote)
        }

    }

    fun saveNote() {
        if (titleEditText.value.isNullOrEmpty() || descriptionEditText.value.isNullOrEmpty()) {
            _snackBarEvent.value = Event(app.getString(R.string.snackbartext_emptynote))
        } else {
            updateNote()
            viewModelScope.launch {
                if(noteIdentifier==null) {
                    notesRepository.insert(editableNote)
                }else{
                    notesRepository.update(editableNote)
                }
            }
            _navigateUpEvent.value=Event(Unit)
        }
    }

    fun updateNote() {
        editableNote.apply {
            title = titleEditText.value!!
            description = descriptionEditText.value!!
            isFavorite = favoriteCheckBox.value!!
            date = dateTextView.value!!
        }

    }
    fun navigateUp(){
        _navigateUpEvent.value = Event(Unit)
    }

}