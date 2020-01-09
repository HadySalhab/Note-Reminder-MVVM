package com.android.myapplication.todo.ui.edit


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs

import com.android.myapplication.todo.R
import com.android.myapplication.todo.databinding.FragmentNotesEditBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

/**
 * A simple [Fragment] subclass.
 */
class NotesEditFragment : Fragment() {
    private val args by navArgs<NotesEditFragmentArgs>()
    private lateinit var binding:FragmentNotesEditBinding
    private val viewModel:NotesEditViewModel by viewModel {
        parametersOf(args.noteId)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =FragmentNotesEditBinding.inflate(layoutInflater)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }


}
