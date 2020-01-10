package com.android.myapplication.todo.adapters

import android.text.Layout
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.android.myapplication.todo.data.Notes
import com.android.myapplication.todo.databinding.NotesListItemBinding

class NotesListAdapter(
    val onCheckEventListener: (Notes) -> Unit,
    val onNoteClickListener: (Notes) -> Unit
) : ListAdapter<Notes, NotesListAdapter.NotesViewHolder>(NotesDiffUtil()) {
    class NotesViewHolder private constructor(
        val binding: NotesListItemBinding,
        val onCheckEventListener: (Notes) -> Unit,
        val onNoteClickListener: (Notes) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        companion object {
            fun getInstance(
                parent: ViewGroup,
                onCheckEventListener: (Notes) -> Unit,
                onNoteClickListener: (Notes) -> Unit
            ): NotesViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                val binding = NotesListItemBinding.inflate(inflater,parent,false)
                return NotesViewHolder(binding, onCheckEventListener, onNoteClickListener)
            }
        }

        fun bind(note: Notes) {
            binding.note = note
            binding.viewHolder = this
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesViewHolder =
        NotesViewHolder.getInstance(parent, onCheckEventListener, onNoteClickListener)

    override fun onBindViewHolder(holder: NotesViewHolder, position: Int) {
        val note = getItem(position)
        note?.let {
            holder.bind(it)
        }
    }

}

class NotesDiffUtil : DiffUtil.ItemCallback<Notes>() {
    override fun areItemsTheSame(oldItem: Notes, newItem: Notes): Boolean {
        return oldItem.noteIdentifier == newItem.noteIdentifier
    }

    override fun areContentsTheSame(oldItem: Notes, newItem: Notes): Boolean {
        //data class compares each of the property to see if they are equal
        return oldItem == newItem


    }

}