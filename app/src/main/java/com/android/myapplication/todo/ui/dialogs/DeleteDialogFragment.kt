package com.android.myapplication.todo.ui.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.android.myapplication.todo.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class DeleteDialogFragment :DialogFragment(){
    interface Callbacks {
        fun onPositiveButtonClick()
    }
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
         super.onCreateDialog(savedInstanceState)
        val clickListener = DialogInterface.OnClickListener{dialog,id->
            targetFragment?.let { fragment ->
                (fragment as Callbacks).onPositiveButtonClick()
            }
        }
        val alertDialogBuilder = MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.delete_message)
            .setPositiveButton(R.string.delete_dialog_positive_button,clickListener)
            .setNegativeButton(R.string.delete_dialog_negative_button,null)
        return alertDialogBuilder.create()
    }
}