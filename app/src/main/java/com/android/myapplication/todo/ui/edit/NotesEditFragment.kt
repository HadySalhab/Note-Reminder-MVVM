package com.android.myapplication.todo.ui.edit


import android.os.Bundle
import android.text.TextUtils
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.android.myapplication.todo.R
import com.android.myapplication.todo.databinding.FragmentNotesEditBinding
import com.android.myapplication.todo.util.EventObserver
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

/**
 * A simple [Fragment] subclass.
 */
class NotesEditFragment : Fragment() {
    private val args by navArgs<NotesEditFragmentArgs>()
    private lateinit var binding: FragmentNotesEditBinding
    private val viewModel: NotesEditViewModel by viewModel {
        parametersOf(args.noteId)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNotesEditBinding.inflate(layoutInflater)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        setuptoolbar()
        setHasOptionsMenu(true)
        return binding.root
    }
    fun setuptoolbar() {
        (requireActivity() as AppCompatActivity).apply {
            setSupportActionBar(binding.editToolbar)
            supportActionBar?.setDisplayShowTitleEnabled(false)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.edit_optionmenu, menu)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.snackBarEvent.observe(viewLifecycleOwner, EventObserver { message ->
            if (!TextUtils.isEmpty(message)) {
                showSnackBar(message)
            }
        })
    }

    fun showSnackBar(message: String) {
        Snackbar.make(
            requireActivity().findViewById<CoordinatorLayout>(R.id.edit_coordinatorlayout),
            message,
            Snackbar.LENGTH_LONG
        ).show()
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.edit_item_save -> {
                viewModel.saveNote()
                true
            }
            R.id.edit_item_date -> {
                viewModel.deleteNote()
                true
            }
            R.id.edit_item_delete -> {
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    override fun onStop() {
        super.onStop()
        deleteEmptyNote()
    }

    fun deleteEmptyNote() {
        viewModel.deleteEmptyNote()
    }


}
