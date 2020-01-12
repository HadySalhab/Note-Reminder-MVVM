package com.android.myapplication.todo.ui.list


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import com.android.myapplication.todo.R
import com.android.myapplication.todo.adapters.NotesListAdapter
import com.android.myapplication.todo.data.Notes
import com.android.myapplication.todo.databinding.FragmentNotesListBinding
import org.koin.androidx.viewmodel.ext.android.viewModel


class NotesListFragment : Fragment() {
    companion object {
        private const val TAG = "NotesListFragment"
    }

    interface Callbacks {
        fun onNoteClick(note: Notes)
    }

    val viewModel: NotesListViewModel by viewModel()
    private lateinit var binding: FragmentNotesListBinding

    private val onCheckChangedListener: (Notes) -> Unit = { note ->
        val newNote = note.copy(isFavorite = !note.isFavorite)
        viewModel.updateNote(newNote)
    }
    private val onNoteClickListener: (Notes) -> Unit = { note ->
        parentFragment?.let { parentFragment ->
            (parentFragment as Callbacks).onNoteClick(note)
        }
    }

    private lateinit var noteAdapter: NotesListAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        noteAdapter = NotesListAdapter(onCheckChangedListener, onNoteClickListener)
        binding = FragmentNotesListBinding.inflate(layoutInflater, container, false)
        binding.recyclerview.apply {
            adapter = noteAdapter
            addItemDecoration(DividerItemDecoration(activity, DividerItemDecoration.VERTICAL))
        }
        setupSpinner()
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.notes.observe(viewLifecycleOwner, Observer { notes ->
            Log.d(TAG, "onActivityCreated: ${notes}")
        })
    }

    fun setupSpinner() {
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.filter_spinner,
            R.layout.spinner_layout
        ).also { arrayAdapter ->
            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.filteringSpinner.adapter = arrayAdapter
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.updateSelectedSpinnerPosition()
    }


}
