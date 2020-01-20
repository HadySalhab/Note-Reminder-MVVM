package com.android.myapplication.todo.ui.list


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import com.android.myapplication.todo.R
import com.android.myapplication.todo.adapters.RemindersListAdapter
import com.android.myapplication.todo.data.Reminders
import com.android.myapplication.todo.databinding.FragmentRemindersListBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * A simple [Fragment] subclass.
 */
class RemindersListFragment : Fragment() {
    companion object {
        private const val TAG = "RemindersListFragment"
    }

    interface Callbacks {
        fun onReminderClick(reminder: Reminders)
    }
    val viewModel: RemindersListViewModel by viewModel()
    private lateinit var binding: FragmentRemindersListBinding

    private val onCheckChangedListener: (Reminders) -> Unit = { reminder ->
        val newReminder = reminder.copy(isActive = !reminder.isActive)
        viewModel.updateReminder(newReminder)
        viewModel.updateAlarm(newReminder)
    }

    private val onReminderClickListener: (Reminders) -> Unit = { reminder ->
        parentFragment?.let { parentFragment ->
            (parentFragment as Callbacks).onReminderClick(reminder)
        }
    }
    private lateinit var reminderAdapter: RemindersListAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        reminderAdapter = RemindersListAdapter(onCheckChangedListener, onReminderClickListener)
        binding = FragmentRemindersListBinding.inflate(layoutInflater, container, false)
        binding.remindersList.apply {
            adapter = reminderAdapter
            addItemDecoration(DividerItemDecoration(activity, DividerItemDecoration.VERTICAL))
        }
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }


}
