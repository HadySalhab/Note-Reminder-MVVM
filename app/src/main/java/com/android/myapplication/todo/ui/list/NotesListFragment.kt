package com.android.myapplication.todo.ui.list


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.myapplication.todo.data.Notes

import com.android.myapplication.todo.databinding.FragmentNotesListBinding
import org.koin.androidx.viewmodel.ext.android.viewModel


class NotesListFragment : Fragment() {
    val viewModel:NotesListViewModel by viewModel ()
    private lateinit var binding:FragmentNotesListBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNotesListBinding.inflate(layoutInflater)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }


}
