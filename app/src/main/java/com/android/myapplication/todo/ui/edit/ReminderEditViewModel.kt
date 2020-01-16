package com.android.myapplication.todo.ui.edit

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.android.myapplication.todo.data.Reminders
import com.android.myapplication.todo.repositories.Repository
import kotlinx.coroutines.launch

class ReminderEditViewModel(
    private val repository: Repository,
    private val reminderIdentifier: String?,
    private val app: Application
) : AndroidViewModel(app) {
    private lateinit var editableReminder: Reminders

    companion object {
        private const val TAG = "ReminderEditViewModel"
    }

    init {
        initializeReminder()
    }

    val _titleEditText = MutableLiveData<String>() //2-way binding
    val _switchRepeat = MutableLiveData<Boolean>() //2-way binding
    private val _dateText = MutableLiveData<String>()
    val dateText:LiveData<String>
    get() = _dateText
    private val _timeText = MutableLiveData<String>()
    val timeText:LiveData<String>
        get() = _timeText
    private val _repeatIntervalText = MutableLiveData<String>()
    val repeatIntervalText:LiveData<String>
        get() = _repeatIntervalText
    private val _repeatIntervalUnitText = MutableLiveData<String>()
    val repeatIntervalUnitText:LiveData<String>
        get() = _repeatIntervalUnitText


    fun initializeReminder() {
        viewModelScope.launch {
            if (reminderIdentifier != null) {
                editableReminder= repository.getReminderById(reminderIdentifier) ?: Reminders()
            } else {
                editableReminder = Reminders()
            }
            Log.d(TAG, "showlog: identifier is ${editableReminder.reminderIndentifier}")
            updateUI()
        }
    }

    fun updateUI() {
        _titleEditText.value = editableReminder.title
        _switchRepeat.value = editableReminder.repeat
        _dateText.value = editableReminder.date
        _timeText.value = editableReminder.time
        _repeatIntervalText.value = editableReminder.repeatValue.toString()
        _repeatIntervalUnitText.value = editableReminder.repeatUnit
    }
}