package com.android.myapplication.todo.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.myapplication.todo.util.Event

class HomeViewPagerViewModel : ViewModel() {

    private val _fabNavListenner= MutableLiveData<Event<Unit>>()
    val fabNavListenner:LiveData<Event<Unit>>
    get() = _fabNavListenner


    fun fabNavTriggered(){
        _fabNavListenner.value = Event(Unit)
    }




}