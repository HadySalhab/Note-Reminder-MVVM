package com.android.myapplication.todo.ui.display

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.android.myapplication.todo.R
import com.android.myapplication.todo.databinding.FragmentNotesDisplayBinding
import com.android.myapplication.todo.databinding.FragmentNotesEditBinding
import com.android.myapplication.todo.ui.edit.NotesEditViewModel
import com.android.myapplication.todo.util.Destination
import com.android.myapplication.todo.util.EventObserver
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class NotesDisplayFragment : Fragment() {
    private lateinit var navController: NavController
    private val args by navArgs<NotesDisplayFragmentArgs>()
    private lateinit var binding: FragmentNotesDisplayBinding
    private lateinit var appBarConfig: AppBarConfiguration
    private val viewModel: NotesDisplayViewModel by viewModel {
        parametersOf(args.noteIdentifier)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        navController = findNavController()

        binding = FragmentNotesDisplayBinding.inflate(layoutInflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        setuptoolbar()
        setHasOptionsMenu(true)
        return binding.root
    }

    fun setuptoolbar() {
        (requireActivity() as AppCompatActivity).apply {
            setSupportActionBar(binding.displayToolbar)
            supportActionBar?.setDisplayShowTitleEnabled(false)
            appBarConfig = AppBarConfiguration(navController.graph)
            setupActionBarWithNavController(navController, appBarConfig)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.display_optionmenu, menu)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.navigationEvent.observe(viewLifecycleOwner,EventObserver{destination->
            when(destination){
                Destination.EDITFRAGMENT->{
                    val action = NotesDisplayFragmentDirections.actionNotesDisplayFragmentToNotesEditFragment(args.noteIdentifier)
                    navController.navigate(action)
                }
                Destination.UP->{
                    navController.navigateUp()
                }
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.display_item_edit -> {
                viewModel.navigateToEdit()
                true
            }
            android.R.id.home->{
                viewModel.navigateUp()
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }


    }
}
