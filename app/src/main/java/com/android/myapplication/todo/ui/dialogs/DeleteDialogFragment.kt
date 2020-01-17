package com.android.myapplication.todo.ui.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.android.myapplication.todo.R
import com.android.myapplication.todo.util.ARG_DELETE_MESSAGE
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class DeleteDialogFragment private constructor(): DialogFragment() {
    interface Callbacks {
        fun onPositiveButtonClick()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        super.onCreateDialog(savedInstanceState)
        val deleteMessage = arguments?.getString(ARG_DELETE_MESSAGE)
        val clickListener = DialogInterface.OnClickListener { dialog, id ->
            targetFragment?.let { fragment ->
                (fragment as Callbacks).onPositiveButtonClick()
            }
        }
        val alertDialogBuilder = MaterialAlertDialogBuilder(requireContext())
            .setTitle(deleteMessage)
            .setPositiveButton(R.string.delete_dialog_positive_button, clickListener)
            .setNegativeButton(R.string.delete_dialog_negative_button, null)
        return alertDialogBuilder.create()
    }

    companion object {
        fun newInstance(message: String): DeleteDialogFragment {
            val args = Bundle().apply {
                putString(ARG_DELETE_MESSAGE, message)
            }

            return DeleteDialogFragment().apply {
                arguments = args
            }
        }
    }
}