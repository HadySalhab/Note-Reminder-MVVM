package com.android.myapplication.todo.ui.edit


import android.os.Bundle
import android.text.TextUtils
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.android.myapplication.todo.R
import com.android.myapplication.todo.databinding.FragmentEditReminderBinding
import com.android.myapplication.todo.ui.dialogs.*
import com.android.myapplication.todo.util.*
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

/**
 * A simple [Fragment] subclass.
 */
class ReminderEditFragment : Fragment(),
    DeleteDialogFragment.Callbacks,
    DatePickerFragment.Callbacks,
    TimePickerFragment.Callbacks,
    ReminderUnitDialog.Callbacks,
    ReminderValueDialog.Callbacks {

    private val args by navArgs<ReminderEditFragmentArgs>()
    private lateinit var binding: FragmentEditReminderBinding
    private lateinit var navController: NavController
    private lateinit var appBarConfig: AppBarConfiguration
    private val viewModel: ReminderEditViewModel by viewModel {
        parametersOf(args.reminderId)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        navController = findNavController()
        binding = FragmentEditReminderBinding.inflate(layoutInflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        setuptoolbar()
        setHasOptionsMenu(true)
        return binding.root
    }

    fun setuptoolbar() {
        (requireActivity() as AppCompatActivity).apply {
            setSupportActionBar(binding.reminderToolbar)
            appBarConfig = AppBarConfiguration(navController.graph)
            setupActionBarWithNavController(navController, appBarConfig)
            supportActionBar?.apply {
                setDisplayShowTitleEnabled(false)
                setHomeAsUpIndicator(R.drawable.ic_close)
            }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.navigationEvent.observe(viewLifecycleOwner, EventObserver { destination ->
            when (destination) {
                Destination.UP -> {
                    navController.navigateUp()
                }
            }
        })
        viewModel.snackBarEvent.observe(viewLifecycleOwner, EventObserver { message ->
            if (!TextUtils.isEmpty(message)) {
                showSnackBar(message, null)
            }
        })

        viewModel.showDeleteDialogEvent.observe(viewLifecycleOwner, EventObserver { message ->
            DeleteDialogFragment.newInstance(message).apply {
                setTargetFragment(this@ReminderEditFragment, REQUEST_DELETE_ANSWER)
                show(this@ReminderEditFragment.requireFragmentManager(), DIALOG_DELETE)
            }
        })
        viewModel.showDatePickerEvent.observe(viewLifecycleOwner, EventObserver { date ->
            DatePickerFragment.newInstance(date).apply {
                setTargetFragment(this@ReminderEditFragment, REQUEST_DATE)
                show(this@ReminderEditFragment.requireFragmentManager(), DIALOG_DATE)
            }
        })

        viewModel.showTimePickerEvent.observe(viewLifecycleOwner, EventObserver { time ->
            TimePickerFragment.newInstance(time).apply {
                setTargetFragment(this@ReminderEditFragment, REQUEST_TIME)
                show(this@ReminderEditFragment.requireFragmentManager(), DIALOG_TIME)
            }
        })

        viewModel.showEditDialogEvent.observe(viewLifecycleOwner, EventObserver { value ->
            ReminderValueDialog.newInstance(value).apply {
                setTargetFragment(this@ReminderEditFragment, REQUEST_REPEAT_VALUE)
                show(this@ReminderEditFragment.requireFragmentManager(), DIALOG_REAPEAT_VALUE)
            }
        })

        viewModel.showListDialogEvent.observe(viewLifecycleOwner, EventObserver {
            ReminderUnitDialog().apply {
                setTargetFragment(this@ReminderEditFragment, REQUEST_REPEAT_UNIT)
                show(this@ReminderEditFragment.requireFragmentManager(), DIALOG_REAPEAT_UNIT)
            }
        })


    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.edit_reminder_optionmenu, menu)
        menu.findItem(R.id.edit_reminder_delete).isVisible = args.reminderId != null
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {

            R.id.edit_reminder_delete -> {
                viewModel.showDeleteDialog()
                true
            }

            R.id.edit_reminder_check -> {
                viewModel.saveReminder()
                true
            }

            android.R.id.home -> {
                viewModel.navigateUp()
                true
            }

            else -> {
                super.onOptionsItemSelected(item)
            }
        }

    }

    override fun onPositiveButtonClick() {
        viewModel.deleteAndNavigateToList()
    }

    fun showSnackBar(message: String, actionMessage: String?) {
        val snackbar = Snackbar.make(
            binding.reminderEditCoordinatorLayout,
            message,
            Snackbar.LENGTH_LONG
        )
        snackbar.show()
    }

    override fun onDateSelected(date: String) {
        viewModel.updateDateTextView(date)
    }

    override fun onTimeSelected(time: String) {
        viewModel.updateTimeTextView(time)
    }

    override fun onItemSelected(item: String) {
        viewModel.updateReminderUnit(item)
    }

    override fun onValueSelected(valueInput: String) {
        viewModel.updateReminderValue(valueInput)
    }

}



