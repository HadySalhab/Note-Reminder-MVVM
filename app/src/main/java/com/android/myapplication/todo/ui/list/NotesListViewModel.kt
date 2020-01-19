package com.android.myapplication.todo.ui.list

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.android.myapplication.todo.R
import com.android.myapplication.todo.data.Notes
import com.android.myapplication.todo.preferences.PreferencesStorage
import com.android.myapplication.todo.repositories.Repository
import com.android.myapplication.todo.util.Filter
import kotlinx.coroutines.launch


class NotesListViewModel(private val repository: Repository, val app: Application) :
    AndroidViewModel(app) {
    companion object {
        private const val TAG = "NotesListViewModel"
    }
    
    val _itemPosition = MutableLiveData<Int>(getInitialPosition())
    fun getInitialPosition(): Int = PreferencesStorage.getStoredPosition(app)

    private val _filter = Transformations.map(_itemPosition) { position ->

        when (position) {
            0 -> {
                Filter.ALL
            }
            1 -> {
                Filter.FAVORITES
            }
            else -> {
                Filter.ALL
            }
        }
    }
    val notes = Transformations.switchMap(_filter) { filterValue ->
        when (filterValue) {
            Filter.ALL -> {
                repository.getAllNotes()
            }
            Filter.FAVORITES -> {
                repository.getFavoriteNotes()
            }
            else -> {
                repository.getAllNotes()
            }
        }

    }

    val isEmpty = Transformations.map(notes) {
        it.isEmpty()
    }


    val imagePlaceHolder = Transformations.map(_filter) { filterValue ->
        when (filterValue) {
            Filter.ALL -> {
                app.resources.getDrawable(R.drawable.no_notes_logo, null)
            }
            Filter.FAVORITES -> {
                app.resources.getDrawable(R.drawable.no_favorite_notes, null)
            }
            else -> {
                app.resources.getDrawable(R.drawable.no_notes_logo, null)
            }
        }
    }
    val textPlaceHolder = Transformations.map(_filter) { filterValue ->
        when (filterValue) {
            Filter.ALL -> {
                app.resources.getString(R.string.no_notes_text_placeholder)
            }
            Filter.FAVORITES -> {
                app.resources.getString(R.string.no_favorite_notes_text_placeholder)
            }
            else -> {
                app.resources.getString(R.string.no_notes_text_placeholder)
            }
        }
    }


    fun updateNote(note: Notes) {
        viewModelScope.launch {
            repository.update(note)
            Log.d(TAG, "updateNote: ")
        }
    }


    fun updateSelectedSpinnerPosition() {
        PreferencesStorage.setStoredPosition(app, _itemPosition.value!!)
    }


}