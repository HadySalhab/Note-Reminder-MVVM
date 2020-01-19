package com.android.myapplication.todo.ui.dialogs

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.DatePicker
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment
import com.android.myapplication.todo.util.ARG_NOTE_DATE
import com.android.myapplication.todo.util.ARG_TIME
import java.text.SimpleDateFormat
import java.util.*

class TimePickerFragment private constructor():DialogFragment(){
    interface Callbacks {
        fun onTimeSelected(time: String)
    }
    @SuppressLint("SimpleDateFormat")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val timeListener = TimePickerDialog.OnTimeSetListener {
                _: TimePicker, hourOfDay: Int,minute:Int  ->

            val resultTime = GregorianCalendar(0, 0, 0,hourOfDay,minute).time
            val resultTimeString = SimpleDateFormat("h:mm a").format(resultTime)
            targetFragment?.let { fragment ->
                (fragment as Callbacks).onTimeSelected(resultTimeString)
            }
        }

        val timeString = arguments?.getString(ARG_TIME) ?: SimpleDateFormat("h:mm a").format(Date())
        val time = SimpleDateFormat("h:mm a").parse(timeString)
        val calendar = Calendar.getInstance()
        calendar.time = time ?: Date()
        val initialHourOfDay = calendar.get(Calendar.HOUR_OF_DAY)
        val initialMinute = calendar.get(Calendar.MINUTE)

        return TimePickerDialog(
            requireContext(),
            timeListener,
            initialHourOfDay,
            initialMinute,
            true
        )
    }

    companion object {
        fun newInstance(time: String): TimePickerFragment {
            val args = Bundle().apply {
                putString(ARG_TIME,time )
            }

            return TimePickerFragment().apply {
                arguments = args
            }
        }
    }

}