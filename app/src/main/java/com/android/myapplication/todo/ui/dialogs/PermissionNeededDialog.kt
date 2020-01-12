package com.android.myapplication.todo.ui.dialogs


import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.android.myapplication.todo.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class PermissionNeededDialog :DialogFragment(){
    interface Callbacks {
        fun onNegativeButtonClick()
    }
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        super.onCreateDialog(savedInstanceState)
        val clickListener = DialogInterface.OnClickListener{dialog,id->
            targetFragment?.let { fragment ->
                (fragment as Callbacks).onNegativeButtonClick()
            }
        }
        val alertDialogBuilder = MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.permission_dialog_title)
            .setMessage(R.string.permission_dialog_message)
            .setPositiveButton(R.string.permission_dialog_positive,null)
            .setNegativeButton(R.string.permission_dialog_negative,clickListener)
        return alertDialogBuilder.create()
    }
}