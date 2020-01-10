package com.android.myapplication.todo.ui.edit

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.myapplication.todo.data.Notes
import com.android.myapplication.todo.repositories.NotesRepository
import kotlinx.coroutines.launch

class NotesEditViewModel (private val notesRepository: NotesRepository,
                          private val noteId:Int) : ViewModel(){

    companion object {
        private const val TAG = "NotesEditViewModel"
    }
    private lateinit var editableNote:Notes
    init {
        initializeNote()
    }


    val titleEditText= MutableLiveData<String>()

    val descriptionEditText=MutableLiveData<String>()

    val favoriteCheckBox =MutableLiveData<Boolean>()

    val dateTextView = MutableLiveData<String>()



    fun initializeNote(){
        viewModelScope.launch {
            editableNote = notesRepository.getNoteById(noteId)?:Notes()
            updateUI()
        }
    }
    fun updateUI(){
        titleEditText.value = editableNote.title
        descriptionEditText.value = editableNote.description
        favoriteCheckBox.value = editableNote.isFavorite
        dateTextView.value = editableNote.date
    }

    override fun onCleared() {
        super.onCleared()
        showlog()
    }

    fun showlog(){
        Log.d(TAG, "showlog: title is ${titleEditText.value}")
        Log.d(TAG, "showlog: desc is ${descriptionEditText.value}")
        Log.d(TAG, "showlog: isfav ${favoriteCheckBox.value}")
        Log.d(TAG, "showlog: date is ${dateTextView.value}")
    }

    fun deleteEmptyNote(){
        if(titleEditText.value.isNullOrEmpty() || descriptionEditText.value.isNullOrEmpty()){
            viewModelScope.launch {
                notesRepository.delete(editableNote)
            }

        }
    }


}