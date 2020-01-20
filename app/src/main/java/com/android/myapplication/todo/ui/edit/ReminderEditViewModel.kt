package com.android.myapplication.todo.ui.edit

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.Application
import android.content.Context
import android.util.Log
import androidx.core.app.AlarmManagerCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.android.myapplication.todo.R
import com.android.myapplication.todo.data.Reminders
import com.android.myapplication.todo.receiver.AlarmReceiver
import com.android.myapplication.todo.repositories.Repository
import com.android.myapplication.todo.util.*
import kotlinx.coroutines.launch

class ReminderEditViewModel(
    private val repository: Repository,
    private val reminderIdentifier: String?,
    private val app: Application
) : AndroidViewModel(app) {

    private lateinit var editableReminder: Reminders

    private val alarmManager = app.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    companion object {
        private const val TAG = "ReminderEditViewModel"
    }

    init {
        initializeReminder()
    }

    val _titleEditText = MutableLiveData<String>() //2-way binding
    val _switchRepeat = MutableLiveData<Boolean>() //2-way binding

    private val _dateText = MutableLiveData<String>()
    val dateText: LiveData<String>
        get() = _dateText

    private val _timeText = MutableLiveData<String>()
    val timeText: LiveData<String>
        get() = _timeText

    private val _repeatIntervalText = MutableLiveData<String>()
    val repeatIntervalText: LiveData<String>
        get() = _repeatIntervalText

    private val _repeatIntervalUnitText = MutableLiveData<String>()
    val repeatIntervalUnitText: LiveData<String>
        get() = _repeatIntervalUnitText

    private val _navigationEvent = MutableLiveData<Event<Destination>>()
    val navigationEvent: LiveData<Event<Destination>>
        get() = _navigationEvent

    private val _snackBarEvent = MutableLiveData<Event<String>>()
    val snackBarEvent: LiveData<Event<String>>
        get() = _snackBarEvent

    private val _showDeleteDialogEvent = MutableLiveData<Event<String>>()
    val showDeleteDialogEvent: LiveData<Event<String>>
        get() = _showDeleteDialogEvent

    private val _showDatePickerEvent = MutableLiveData<Event<String>>()
    val showDatePickerEvent: LiveData<Event<String>>
        get() = _showDatePickerEvent

    private val _showTimePickerEvent = MutableLiveData<Event<String>>()
    val showTimePickerEvent: LiveData<Event<String>>
        get() = _showTimePickerEvent

    private val _showEditDialogEvent = MutableLiveData<Event<String>>()
    val showEditDialogEvent: LiveData<Event<String>>
        get() = _showEditDialogEvent

    private val _showListDialogEvent = MutableLiveData<Event<Unit>>()
    val showListDialogEvent: LiveData<Event<Unit>>
        get() = _showListDialogEvent


    fun initializeReminder() {
        viewModelScope.launch {
            if (reminderIdentifier != null) {
                editableReminder = repository.getReminderById(reminderIdentifier) ?: Reminders()
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

    fun navigateUp() {
        _navigationEvent.value = Event(Destination.UP)
    }

    fun showDeleteDialog() {
        _showDeleteDialogEvent.value = Event(app.getString(R.string.delete_reminder_message))
    }

    fun saveReminder() {
        if (_titleEditText.value.isNullOrEmpty()) {
            _snackBarEvent.value = Event(app.getString(R.string.snackbartext_emptyReminder))
        } else {
            updateReminder()
            viewModelScope.launch {
                if (reminderIdentifier == null) {
                    repository.insert(editableReminder)
                    editableReminder = repository.getLatestReminder()!!
                } else {
                    repository.update(editableReminder)
                    cancelExistingAlarm()
                }
                createReminderAlarm()
                _navigationEvent.value = Event(Destination.UP)
            }
        }
    }

    fun updateReminder() {
        editableReminder.apply {
            title = _titleEditText.value!!
            date = _dateText.value!!
            time = _timeText.value!!
            repeat = _switchRepeat.value!!
            repeatValue = _repeatIntervalText.value!!.toInt()
            repeatUnit = _repeatIntervalUnitText.value!!
        }
    }

    fun deleteAndNavigateToList() {
        viewModelScope.launch {
            repository.delete(editableReminder)
            cancelExistingAlarm()
            _navigationEvent.value = Event(Destination.UP)
        }
    }

    fun showDatePicker() {
        _showDatePickerEvent.value = Event(_dateText.value!!)
    }

    fun showTimePicker() {
        _showTimePickerEvent.value = Event(_timeText.value!!)
    }

    fun showEditDialog() {
        _showEditDialogEvent.value = Event(_repeatIntervalText.value!!)
    }

    fun showListDialog() {
        _showListDialogEvent.value = Event(Unit)
    }

    fun updateDateTextView(date: String) {
        _dateText.value = date
    }

    fun updateTimeTextView(time: String) {
        _timeText.value = time
    }

    fun updateReminderUnit(item: String) {
        _repeatIntervalUnitText.value = item
    }

    fun updateReminderValue(valueInput: String) {
        _repeatIntervalText.value = valueInput
    }

    fun createReminderAlarm() {
        if (editableReminder.isActive) {
            AlarmUtil.createAlarm(
                app.applicationContext,
                editableReminder,
                alarmManager
            )
        }
    }

    fun cancelExistingAlarm() {
        AlarmUtil.cancelAlarm(
            app.applicationContext,
            editableReminder,
            alarmManager)
    }






}
