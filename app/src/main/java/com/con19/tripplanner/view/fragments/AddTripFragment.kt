package com.con19.tripplanner.view.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.EditText
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.con19.tripplanner.R
import com.con19.tripplanner.db.entities.Person
import com.con19.tripplanner.db.entities.Trip
import com.con19.tripplanner.db.entities.TripWithPeople
import com.con19.tripplanner.viewmodel.PersonViewModel
import com.con19.tripplanner.viewmodel.TripViewModel
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.datepicker.MaterialDatePicker
import java.util.*


open class AddTripFragment : Fragment() {

    protected var listener: AddTripFragmentListener? = null
    private lateinit var tripViewModel: TripViewModel
    private lateinit var personViewModel: PersonViewModel
    private var allPeople: List<Person>? = null

    private lateinit var nameEditText: EditText
    private lateinit var dateEditText: EditText
    private lateinit var addPersonEditText: AutoCompleteTextView
    private lateinit var chipGroup: ChipGroup
    private lateinit var tripCoverPhoto: ImageView
    protected lateinit var toolbar: MaterialToolbar
    private var addedPeople: MutableList<Person> = mutableListOf()

    private lateinit var startDate : Date
    private lateinit var endDate : Date

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
        listener?.onTripFragmentBackButtonPressed()
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
        }
    }

    protected open fun submitTrip() {
        val name = nameEditText.text.toString()
        val newTrip = tripViewModel.insertAsync(
            Trip(name, startDate, endDate, null),
            addedPeople
        )
        listener?.onTripFragmentBackButtonPressed()

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
    }

    interface AddTripFragmentListener {
        fun onTripFragmentBackButtonPressed()
        fun onEditTripBackPressed(tripId: Long)
    }


}