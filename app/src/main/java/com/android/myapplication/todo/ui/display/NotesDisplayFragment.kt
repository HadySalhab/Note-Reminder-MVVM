package com.android.myapplication.todo.ui.display

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.android.myapplication.todo.databinding.FragmentNotesDisplayBinding
import com.android.myapplication.todo.databinding.FragmentNotesEditBinding
import com.android.myapplication.todo.ui.edit.NotesEditViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class NotesDisplayFragment : Fragment() {
    private val args by navArgs<NotesDisplayFragmentArgs>()
    private lateinit var binding: FragmentNotesDisplayBinding
    private val viewModel: NotesDisplayViewModel by viewModel {
        parametersOf(args.noteId)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNotesDisplayBinding.inflate(layoutInflater)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }
}
