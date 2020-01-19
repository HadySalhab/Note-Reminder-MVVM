package com.android.myapplication.todo.ui.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.text.InputType
import android.text.TextUtils
import android.widget.EditText
import androidx.core.view.marginStart
import androidx.fragment.app.DialogFragment
import com.android.myapplication.todo.R
import com.android.myapplication.todo.util.ARG_DELETE_MESSAGE
import com.android.myapplication.todo.util.ARG_REMINDER_REPEAT_VALUE
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class ReminderValueDialog private constructor(): DialogFragment() {
    interface Callbacks {
        fun onValueSelected(valueInput:String)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        super.onCreateDialog(savedInstanceState)
        val value = arguments?.getString(ARG_REMINDER_REPEAT_VALUE)
        val editText = EditText(requireContext())
        editText.apply {
            setText(value)
            setInputType(InputType.TYPE_CLASS_NUMBER)
        }
            val clickListener = DialogInterface.OnClickListener { dialog, id ->
                targetFragment?.let { fragment ->
                    var valueInput = editText.text.toString()
                    if(TextUtils.isEmpty(valueInput)){
                        valueInput = "1"
                    }else{
                        valueInput = valueInput.trim()
                    }
                    (fragment as ReminderValueDialog.Callbacks).onValueSelected(valueInput)
                }
            }
            val alertDialogBuilder = MaterialAlertDialogBuilder(requireContext())
                .setTitle(getString(R.string.repeat_value_dialog_message))
                .setView(editText)
                .setPositiveButton(getString(R.string.value_dialog_positive_btn), clickListener)
                .setNegativeButton(getString(R.string.value_dialog_negative_btn), null)
            return alertDialogBuilder.create()
        }
    companion object {
        fun newInstance(value: String): ReminderValueDialog {
            val args = Bundle().apply {
                putString(ARG_REMINDER_REPEAT_VALUE, value)
            }

            return ReminderValueDialog().apply {
                arguments = args
            }
        }
    }
}