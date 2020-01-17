package com.android.myapplication.todo.ui.edit


import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.text.TextUtils
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.android.myapplication.todo.R
import com.android.myapplication.todo.databinding.FragmentNotesEditBinding
import com.android.myapplication.todo.ui.dialogs.DatePickerFragment
import com.android.myapplication.todo.ui.dialogs.DeleteDialogFragment
import com.android.myapplication.todo.ui.dialogs.PermissionNeededDialog
import com.android.myapplication.todo.util.*
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

/**
 * A simple [Fragment] subclass.
 */
class NotesEditFragment : Fragment(), DatePickerFragment.Callbacks, DeleteDialogFragment.Callbacks,
    PermissionNeededDialog.Callbacks {
    private val args by navArgs<NotesEditFragmentArgs>()
    private lateinit var binding: FragmentNotesEditBinding
    private lateinit var navController: NavController
    private lateinit var appBarConfig: AppBarConfiguration
    private lateinit var photoUri: Uri
    private val viewModel: NotesEditViewModel by viewModel {
        parametersOf(args.noteIdentifier)
    }

    companion object {
        private const val TAG = "NotesEditFragment"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        navController = findNavController()
        binding = FragmentNotesEditBinding.inflate(layoutInflater, container, false)
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
            appBarConfig = AppBarConfiguration(navController.graph)
            setupActionBarWithNavController(navController, appBarConfig)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.edit_optionmenu, menu)
        menu.findItem(R.id.edit_item_delete).isVisible = args.noteIdentifier != null
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.snackBarEvent.observe(viewLifecycleOwner, EventObserver { message ->
            if (!TextUtils.isEmpty(message)) {
                showSnackBar(message, null)
            }
        })
        viewModel.navigationEvent.observe(viewLifecycleOwner, EventObserver { destination ->
            when (destination) {
                Destination.UP -> {
                    navController.navigateUp()
                }
                Destination.VIEWPAGERFRAGMENT -> {
                    navController.popBackStack(R.id.homeViewPagerFragment, false)
                }
            }
        })

        viewModel.showDatePickerEvent.observe(viewLifecycleOwner, EventObserver { date ->
            DatePickerFragment.newInstance(date).apply {
                setTargetFragment(this@NotesEditFragment, REQUEST_DATE)
                show(this@NotesEditFragment.requireFragmentManager(), DIALOG_DATE)
            }
        })
        viewModel.showDeleteDialogEvent.observe(viewLifecycleOwner, EventObserver {message->
            DeleteDialogFragment.newInstance(message).apply {
                setTargetFragment(this@NotesEditFragment, REQUEST_DELETE_ANSWER)
                show(this@NotesEditFragment.requireFragmentManager(), DIALOG_DELETE)
            }
        })
        viewModel.launchCameraEvent.observe(viewLifecycleOwner, EventObserver { photoUri ->
            this.photoUri = photoUri
            checkCameraPermission()
        })
    }

    fun showSnackBar(message: String, actionMessage: String?) {
        val snackbar = Snackbar.make(
            binding.editCoordinatorlayout,
            message,
            Snackbar.LENGTH_LONG
        )
        actionMessage?.let {
            snackbar.setAction(actionMessage) {
                openAppSetting()
            }
        }
        snackbar.show()
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.edit_item_save -> {
                viewModel.saveNote()
                true
            }
            R.id.edit_item_date -> {
                viewModel.showDatePicker()
                true
            }
            R.id.edit_item_delete -> {
                viewModel.showDeleteDialog()
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

    override fun onDateSelected(date: String) {
        viewModel.updateDateTextView(date)
    }

    override fun onPositiveButtonClick() {
        viewModel.deleteAndNavigateToList()
    }


    fun checkCameraPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    android.Manifest.permission.CAMERA
                ) == PackageManager.PERMISSION_DENIED
            ) {
                //we don't have the permission to use the camera, we should request it
                requestCameraPermission()
            } else {
                //we already have the permission, launch the camera
                launchCamera()
            }
        } else {
            //system os <Marshmallow, permission was granted @ installation time
            launchCamera()
        }
    }

    fun requestCameraPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                requireActivity(),
                Manifest.permission.CAMERA
            )
        ) {
            PermissionNeededDialog().apply {
                setTargetFragment(this@NotesEditFragment, REQUEST_PERMISSION_ANSWER_DIALOG)
                show(this@NotesEditFragment.requireFragmentManager(), DIALOG_PERMISSION)
            }
            if (viewModel.permissionPref) {
                viewModel.changePermissionPref()
            }
        } else {
            requestPermissions(arrayOf(Manifest.permission.CAMERA), PERMISSION_CODE)
        }
    }


    //Handling the request or user response
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSION_CODE -> {
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //permission from popup is granted
                    launchCamera()

                } else {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(
                            requireActivity(),
                            Manifest.permission.CAMERA
                        )
                    ) {
                        Toast.makeText(
                            requireContext(),
                            getString(R.string.permission_denied),
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        if (viewModel.permissionPref) {
                            showSnackBar(
                                getString(R.string.snackbar_permission_message),
                                getString(R.string.snackbar_permission_action_message)
                            )
                        } else {
                            viewModel.changePermissionPref()
                        }
                    }
                }
            }
        }
    }

    fun openAppSetting() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        val uri = Uri.fromParts("package", requireActivity().packageName, null);
        intent.setData(uri);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivityForResult(intent, REQUEST_PERMISSION_SETTING);
    }

    fun launchCamera() {
        Log.d(TAG, "showlog: identifier ${photoUri}")
        captureImageIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
        val cameraActivities: List<ResolveInfo> =
            requireContext().packageManager.queryIntentActivities(
                captureImageIntent,
                PackageManager.MATCH_DEFAULT_ONLY
            )
        for (cameraActivity in cameraActivities) {
            requireActivity().grantUriPermission(
                cameraActivity.activityInfo.packageName,
                photoUri,
                Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION
            )
        }

        startActivityForResult(captureImageIntent, REQUEST_PHOTO)


    }

    override fun onNegativeButtonClick() {
        requestPermissions(arrayOf(Manifest.permission.CAMERA), PERMISSION_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when{
            resultCode!= Activity.RESULT_OK->return
            requestCode == REQUEST_PHOTO->{
                requireActivity().revokeUriPermission(photoUri,Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
                viewModel.updatePhoto()

            }
        }

    }

    override fun onPause() {
        super.onPause()
        viewModel.resetPermissionPref()
    }


}
