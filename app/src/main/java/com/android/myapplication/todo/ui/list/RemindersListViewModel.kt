package com.android.myapplication.todo.ui.list

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.myapplication.todo.data.Notes
import com.android.myapplication.todo.data.Reminders
import com.android.myapplication.todo.repositories.Repository
import kotlinx.coroutines.launch

class RemindersListViewModel(private val repository: Repository) : ViewModel(){

    val reminders = repository.getAllReminders()
    val isEmpty = Transformations.map(reminders) {
        it.isEmpty()
    }

    fun updateReminder(reminder: Reminders) {
        viewModelScope.launch {
            repository.update(reminder)
        }
    }
}