package com.android.myapplication.todo.ui.list


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import com.android.myapplication.todo.adapters.NotesListAdapter
import com.android.myapplication.todo.data.Notes
import com.android.myapplication.todo.databinding.FragmentNotesListBinding
import org.koin.androidx.viewmodel.ext.android.viewModel


class NotesListFragment : Fragment() {
    companion object {
        private const val TAG = "NotesListFragment"
    }

    val viewModel: NotesListViewModel by viewModel()
    private lateinit var binding: FragmentNotesListBinding

    private val onCheckChangedListener: (Notes) -> Unit = { note ->
        viewModel.updateNote(note)
    }
    private val onNoteClickListener: (Notes) -> Unit = { note ->
        val action = NotesListFragmentDirections.actionNotesListFragmentToNotesDisplayFragment(note.id)
        findNavController().navigate(action)
    }
    private lateinit var noteAdapter: NotesListAdapter




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        noteAdapter = NotesListAdapter(onCheckChangedListener, onNoteClickListener)
        binding = FragmentNotesListBinding.inflate(layoutInflater)
        binding.notesList.apply {
            adapter = noteAdapter
            addItemDecoration(DividerItemDecoration(activity, DividerItemDecoration.VERTICAL))
        }
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }



}
