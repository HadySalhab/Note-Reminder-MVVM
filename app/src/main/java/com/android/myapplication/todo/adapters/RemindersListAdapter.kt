package com.android.myapplication.todo.adapters

import android.text.Layout
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.android.myapplication.todo.data.Notes
import com.android.myapplication.todo.data.Reminders
import com.android.myapplication.todo.databinding.NotesListItemBinding
import com.android.myapplication.todo.databinding.ReminderListItemBinding

class RemindersListAdapter(
    val onCheckEventListener: (Reminders) -> Unit,
    val onReminderClickListener: (Reminders) -> Unit
) : ListAdapter<Reminders, RemindersListAdapter.RemindersViewHolder>(RemindersDiffUtil()) {
    class RemindersViewHolder private constructor(
        val binding: ReminderListItemBinding,
        val onCheckEventListener: (Reminders) -> Unit,
        val onReminderClickListener: (Reminders) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        companion object {
            fun getInstance(
                parent: ViewGroup,
                onCheckEventListener: (Reminders) -> Unit,
                onReminderClickListener: (Reminders) -> Unit
            ): RemindersViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                val binding = ReminderListItemBinding.inflate(inflater, parent, false)
                return RemindersViewHolder(binding, onCheckEventListener, onReminderClickListener)
            }
        }

        fun bind(reminder: Reminders) {
            binding.reminder = reminder
            binding.viewHolder = this
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RemindersViewHolder =
        RemindersViewHolder.getInstance(parent, onCheckEventListener, onReminderClickListener)

    override fun onBindViewHolder(holder: RemindersViewHolder, position: Int) {
        val reminders = getItem(position)
        reminders?.let {
            holder.bind(it)
        }
    }

}

class RemindersDiffUtil : DiffUtil.ItemCallback<Reminders>() {
    override fun areItemsTheSame(oldItem: Reminders, newItem: Reminders): Boolean {
        return oldItem.reminderIndentifier == newItem.reminderIndentifier
    }

    override fun areContentsTheSame(oldItem: Reminders, newItem: Reminders): Boolean {
        //data class compares each of the property to see if they are equal
        return oldItem == newItem


    }

}