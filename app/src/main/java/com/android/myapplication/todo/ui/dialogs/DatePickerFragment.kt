package com.android.myapplication.todo.ui.dialogs

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import com.android.myapplication.todo.util.ARG_NOTE_DATE
import java.text.SimpleDateFormat
import java.util.*

class DatePickerFragment private constructor():DialogFragment(){
    interface Callbacks {
        fun onDateSelected(date: String)
    }
    @SuppressLint("SimpleDateFormat")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dateListener = DatePickerDialog.OnDateSetListener {
                _: DatePicker, year: Int, month: Int, day: Int ->

            val resultDate = GregorianCalendar(year, month, day).time
            val resultDateString = SimpleDateFormat("EEE, d MMM yyyy").format(resultDate)
            targetFragment?.let { fragment ->
                (fragment as Callbacks).onDateSelected(resultDateString)
            }
        }

        val dateString = arguments?.getString(ARG_NOTE_DATE) ?: SimpleDateFormat("EEE, d MMM yyyy").format(Date())
        val date = SimpleDateFormat("EEE, d MMM yyyy").parse(dateString)
        val calendar = Calendar.getInstance()
        calendar.time = date ?: Date()
        val initialYear = calendar.get(Calendar.YEAR)
        val initialMonth = calendar.get(Calendar.MONTH)
        val initialDate = calendar.get(Calendar.DAY_OF_MONTH)

        return DatePickerDialog(
            requireContext(),
            dateListener,
            initialYear,
            initialMonth,
            initialDate
        )
    }

    companion object {
        fun newInstance(date: String): DatePickerFragment {
            val args = Bundle().apply {
                putString(ARG_NOTE_DATE,date )
            }

            return DatePickerFragment().apply {
                arguments = args
            }
        }
    }

}