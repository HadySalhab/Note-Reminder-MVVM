package com.android.myapplication.todo.ui.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.text.InputType
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import com.android.myapplication.todo.R
import com.android.myapplication.todo.util.ARG_DELETE_MESSAGE
import com.android.myapplication.todo.util.ARG_REMINDER_REPEAT_UNIT
import com.android.myapplication.todo.util.ARG_REMINDER_REPEAT_VALUE
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class ReminderUnitDialog : DialogFragment() {
    interface Callbacks {
        fun onItemSelected(item:Int)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        super.onCreateDialog(savedInstanceState)
        val items = arrayOf<String>("Month","Week","Day","Hour","Minute")
            val clickListener = DialogInterface.OnClickListener{ dialog, item->
                targetFragment?.let { fragment ->
                    (fragment as ReminderUnitDialog.Callbacks).onItemSelected(item)
                }
            }
            val alertDialogBuilder = MaterialAlertDialogBuilder(requireContext())
                .setItems(items,clickListener)
            return alertDialogBuilder.create()
        }
}