package com.android.myapplication.todo.ui.list

import android.app.Application
import androidx.lifecycle.*
import com.android.myapplication.todo.R
import com.android.myapplication.todo.data.Notes
import com.android.myapplication.todo.repositories.NotesRepository
import com.android.myapplication.todo.util.Filter
import kotlinx.coroutines.launch


class NotesListViewModel(private val notesRepository: NotesRepository,val app:Application) : AndroidViewModel(app){
    private val _filter = MutableLiveData<Filter>(Filter.ALL)
    val notes = Transformations.switchMap(_filter){filterValue->
        when(filterValue){
            Filter.ALL->{
                notesRepository.getAllNotes()
            }
            Filter.FAVORITES->{
                notesRepository.getFavoriteNotes()
            }
            else->{
                notesRepository.getAllNotes()
            }
        }

    }

    val isEmpty = Transformations.map(notes){
        it.isEmpty()
    }

    val category = Transformations.map(_filter){filterValue->
        when(filterValue) {
            Filter.ALL -> {
                app.resources.getString(R.string.category_all)
            }
            Filter.FAVORITES -> {
                app.resources.getString(R.string.category_favorites)
            }
            else -> {
                app.resources.getString(R.string.category_all)
            }
        }
    }
    val imagePlaceHolder = Transformations.map(_filter){filterValue->
        when(filterValue) {
            Filter.ALL -> {
                app.resources.getDrawable(R.drawable.no_notes_logo,null)
            }
            Filter.FAVORITES -> {
                app.resources.getDrawable(R.drawable.no_favorite_notes,null)
            }
            else -> {
                app.resources.getDrawable(R.drawable.no_notes_logo,null)
            }
        }
    }
    val textPlaceHolder=Transformations.map(_filter){filterValue->
        when(filterValue) {
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

    fun updateNote(note: Notes){
        viewModelScope.launch {
            notesRepository.update(note)
        }
    }


}