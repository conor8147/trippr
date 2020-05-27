package com.con19.tripplanner.view.fragments

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.EditText
import android.widget.ImageView
import androidx.core.net.toFile
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.con19.tripplanner.R
import com.con19.tripplanner.db.entities.Person
import com.con19.tripplanner.db.entities.Trip
import com.con19.tripplanner.viewmodel.PersonViewModel
import com.con19.tripplanner.viewmodel.TripViewModel
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.datepicker.MaterialDatePicker
import java.util.*


open class AddTripFragment : Fragment(), CameraFragment.CameraListener {

    protected var listener: AddTripFragmentListener? = null
    private lateinit var tripViewModel: TripViewModel
    private lateinit var personViewModel: PersonViewModel
    private var allPeople: List<Person>? = null

    protected lateinit var nameEditText: EditText
    protected lateinit var dateEditText: EditText
    private lateinit var addPersonEditText: AutoCompleteTextView
    protected lateinit var chipGroup: ChipGroup
    protected lateinit var tripCoverPhoto: ImageView
    protected lateinit var toolbar: MaterialToolbar
    protected var addedPeople: MutableList<Person> = mutableListOf()

    protected var tripPhotoUri: String? = null
    // flag to decide whether to delete the current photo after this fragment is closed.
    protected var tripPhotoSaved = false

    protected lateinit var startDate: Date
    protected lateinit var endDate: Date

    private var cameraFragment: CameraFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        tripViewModel = ViewModelProvider(requireActivity())
            .get(TripViewModel::class.java)
        personViewModel = ViewModelProvider(requireActivity())
            .get(PersonViewModel::class.java)
        allPeople = personViewModel.allPeople.value
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val layout: View = inflater.inflate(R.layout.fragment_add_trip, container, false)
        val builder = MaterialDatePicker.Builder.dateRangePicker()
        val picker = builder.build()
        initViews(layout)

        setToolbarListeners()

        dateEditText.setOnClickListener {
            picker.show(childFragmentManager, picker.toString())
            picker.addOnPositiveButtonClickListener { dates ->
                startDate = dates.first?.let { it -> Date(it) }!!
                endDate = dates.second?.let { it -> Date(it) }!!
            }
        }

        return layout
    }

    protected open fun setToolbarListeners() {
        toolbar.apply {
            setNavigationOnClickListener {
                navigateBack()
            }
            setOnMenuItemClickListener {
                submitTrip()
                true
            }
        }
    }

    protected open fun navigateBack() {
        if (!tripPhotoSaved) {
            deletePhoto(tripPhotoUri)
        }
        listener?.onAddTripFragmentFinished()
    }

    internal open fun initViews(layout: View) {
        nameEditText = layout.findViewById(R.id.tripNameEditText)
        dateEditText = layout.findViewById(R.id.datePicker)
        addPersonEditText = layout.findViewById(R.id.addPersonEditText)
        chipGroup = layout.findViewById(R.id.chipGroup)
        tripCoverPhoto = layout.findViewById(R.id.tripCoverPhoto)
        toolbar = layout.findViewById<MaterialToolbar>(R.id.topAppBar)

        val peopleNames = allPeople?.map { person -> person.nickname }

        val adapter: ArrayAdapter<String>? =
            peopleNames.let { names ->
                names?.let {
                    ArrayAdapter<String>(
                        requireContext(),
                        android.R.layout.simple_dropdown_item_1line,
                        it
                    )
                }
            }

        addPersonEditText.setAdapter(adapter)
        addPersonEditText.apply {
            setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    showDropDown()
                }
            }

            setOnEditorActionListener { v, actionId, event ->
                if (actionId == EditorInfo.IME_ACTION_DONE &&
                    peopleNames?.find { text.toString().toLowerCase() == it.toLowerCase() } != null
                ) {
                    // TODO deal with what to do if person is not already in group
                    val name = text.toString().capitalize()
                    val chip = Chip(context)
                    allPeople?.find { it.nickname.capitalize() == name }
                        ?.let { addedPeople.add(it) }
                    chip.text = name
                    chipGroup.addView(chip)
                    setText("")
                }
                true
            }

            tripCoverPhoto.setOnClickListener {
                openCamera()
            }
        }
    }

    protected open fun submitTrip() {
        val name = nameEditText.text.toString()
        tripViewModel.insertAsync(
            Trip(
                name,
                startDate,
                endDate,
                tripPhotoUri
            ),
            addedPeople
        )
        tripPhotoSaved = true
        listener?.onAddTripFragmentFinished()
    }

    /**
     * Inflates CameraFragment over the current layout.
     */
    private fun openCamera() {
        cameraFragment = CameraFragment.newInstance(PhotoType.transaction)
        val fragManager = childFragmentManager
        val transaction = fragManager.beginTransaction()

        cameraFragment?.let { transaction.add(R.id.camera_container, it) }
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private fun closeCamera() {
        val fragManager = childFragmentManager
        val transaction = fragManager.beginTransaction()
        cameraFragment?.let { transaction.remove(it) }
        transaction.commit()
    }

    override fun onPhotoTaken(uri: Uri) {
        closeCamera()
        tripPhotoUri = uri.toString()
        tripCoverPhoto.setImageURI(uri)
    }

    override fun onCameraError() {
        // TODO Not yet implemented, still not sure if these will be necessary
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is AddTripFragmentListener) {
            this.listener = context
        } else {
            throw RuntimeException("$context must implement AddTripFragmentListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        this.listener = null
        if (!tripPhotoSaved) {
            deletePhoto(tripPhotoUri)
        }
    }

    /**
     * Given the Uri of an image as a string, delete that image.
     */
    protected fun deletePhoto(imageUri: String?) {
        if (imageUri.isNullOrEmpty()) {
            return
        }
        val file = Uri.parse(imageUri).toFile()
        file.delete()
    }


    interface AddTripFragmentListener {
        fun onAddTripFragmentFinished()
        fun onEditTripBackPressed(tripId: Long)
    }

}