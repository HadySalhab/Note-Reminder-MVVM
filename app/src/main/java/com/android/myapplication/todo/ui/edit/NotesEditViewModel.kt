package com.android.myapplication.todo.ui.edit

import android.app.Application
import android.net.Uri
import android.util.Log
import androidx.core.content.FileProvider
import androidx.lifecycle.*
import androidx.preference.PreferenceManager
import com.android.myapplication.todo.data.Notes
import com.android.myapplication.todo.repositories.NotesRepository
import kotlinx.coroutines.launch
import com.android.myapplication.todo.R
import com.android.myapplication.todo.preferences.PreferencesStorage
import com.android.myapplication.todo.util.*
import java.io.File

class NotesEditViewModel(
    private val notesRepository: NotesRepository,
    private val noteIdentifier: String?,
    private val app: Application
) : AndroidViewModel(app) {


    companion object {
        private const val TAG = "NotesEditViewModel"
    }

    private lateinit var editableNote: Notes
    private val _imageFile = MutableLiveData<File>()
    val imageFile: LiveData<File>
        get() = _imageFile

    init {
        initializeNote()
    }

    fun getPermissionPref() = PreferencesStorage.getStoredPermission(app)
    fun resetPermissionPref(value: Boolean) {
        PreferencesStorage.setStoredPermission(app, value)
    }


    val intent = captureImageIntent
    private val _snackBarEvent = MutableLiveData<Event<String>>()
    val snackBarEvent: LiveData<Event<String>>
        get() = _snackBarEvent

    private val _navigationEvent = MutableLiveData<Event<Destination>>()
    val navigationEvent: LiveData<Event<Destination>>
        get() = _navigationEvent

    private val _showDatePickerEvent = MutableLiveData<Event<String>>()
    val showDatePickerEvent: LiveData<Event<String>>
        get() = _showDatePickerEvent

    private val _showDeletDialogEvent = MutableLiveData<Event<Unit>>()
    val showDeleteDialogEvent: LiveData<Event<Unit>>
        get() = _showDeletDialogEvent

    private val _launchCameraEvent = MutableLiveData<Event<Uri>>()
    val launchCameraEvent: LiveData<Event<Uri>>
        get() = _launchCameraEvent


    val _titleEditText = MutableLiveData<String>()

    val _descriptionEditText = MutableLiveData<String>()

    val _favoriteCheckBox = MutableLiveData<Boolean>()

    val _dateTextView = MutableLiveData<String>()


    fun initializeNote() {
        viewModelScope.launch {
            if (noteIdentifier != null) {
                editableNote = notesRepository.getNoteById(noteIdentifier) ?: Notes()
            } else {
                editableNote = Notes()
            }
            Log.d(TAG, "showlog: identifier is ${editableNote.noteIdentifier}")
            updateUI()
        }
    }

    fun updateUI() {
        _titleEditText.value = editableNote.title
        _descriptionEditText.value = editableNote.description
        _favoriteCheckBox.value = editableNote.isFavorite
        _dateTextView.value = editableNote.date
        _imageFile.value = notesRepository.getPhotoFile(editableNote)
    }

    override fun onCleared() {
        super.onCleared()
        showlog()
    }

    fun showlog() {
        Log.d(TAG, "showlog: title is ${_titleEditText.value}")
        Log.d(TAG, "showlog: desc is ${_descriptionEditText.value}")
        Log.d(TAG, "showlog: isfav ${_favoriteCheckBox.value}")
        Log.d(TAG, "showlog: date is ${_dateTextView.value}")
        Log.d(TAG, "showlog: identifier is ${editableNote.noteIdentifier}")
    }


    fun deleteNote() {
        viewModelScope.launch {
            notesRepository.delete(editableNote)
        }

    }

    fun saveNote() {
        if (_titleEditText.value.isNullOrEmpty() || _descriptionEditText.value.isNullOrEmpty()) {
            _snackBarEvent.value = Event(app.getString(R.string.snackbartext_emptynote))
        } else {
            updateNote()
            viewModelScope.launch {
                if (noteIdentifier == null) {
                    notesRepository.insert(editableNote)
                } else {
                    notesRepository.update(editableNote)
                }
            }
            _navigationEvent.value = Event(Destination.UP)
        }
    }

    fun updateNote() {
        editableNote.apply {
            title = _titleEditText.value!!
            description = _descriptionEditText.value!!
            isFavorite = _favoriteCheckBox.value!!
            date = _dateTextView.value!!
        }

    }

    fun navigateUp() {
        _navigationEvent.value = Event(Destination.UP)
    }

    fun showDatePicker() {
        _showDatePickerEvent.value = Event(editableNote.date)
    }

    fun showDeleteDialog() {
        _showDeletDialogEvent.value = Event(Unit)
    }

    fun updateDateTextView(date: String) {
        _dateTextView.value = date
    }

    fun deleteAndNavigateToList() {
        deleteNote()
        _navigationEvent.value = Event(Destination.VIEWPAGERFRAGMENT)
    }

    fun launchCamera() {
        viewModelScope.launch {
            val photoUri = FileProvider.getUriForFile(app, AUTHORITY, _imageFile.value!!)
            _launchCameraEvent.value = Event(photoUri)
        }

    }
    fun updatePhoto(){
        _imageFile.value = notesRepository.getPhotoFile(editableNote)
    }
}