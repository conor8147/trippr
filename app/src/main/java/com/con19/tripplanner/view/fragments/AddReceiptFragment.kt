package com.con19.tripplanner.view.fragments

import android.content.Context
import android.content.res.Resources
import android.os.Bundle
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
import com.con19.tripplanner.db.entities.Transaction
import com.con19.tripplanner.db.entities.TripWithPeople
import com.con19.tripplanner.viewmodel.TransactionViewModel
import com.con19.tripplanner.viewmodel.TripViewModel
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import java.util.*
import kotlin.properties.Delegates


class AddReceiptFragment private constructor() : Fragment() {

    private var listener: AddReceiptFragmentListener? = null

    private var tripId by Delegates.notNull<Long>()
    private var tripWithPeople: TripWithPeople? = null
    private lateinit var transactionViewModel: TransactionViewModel
    private lateinit var tripViewModel: TripViewModel
    private lateinit var nameEditText: EditText
    private lateinit var costEditText: EditText
    private lateinit var addPersonEditText: AutoCompleteTextView
    private lateinit var chipGroup: ChipGroup
    private lateinit var receiptPhoto: ImageView

    private var addedPeople: MutableList<Person> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        tripViewModel = ViewModelProvider(requireActivity())
            .get(TripViewModel::class.java)
        transactionViewModel = ViewModelProvider(requireActivity())
            .get(TransactionViewModel::class.java)

        arguments?.let {
            tripId = it.getLong(TRIP_ID)
        }
        tripWithPeople = tripId.let { tripViewModel.getTripById(it) }

        if (tripWithPeople == null) {
            throw Resources.NotFoundException()
            // TODO: handle this gracefully
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val layout: View = inflater.inflate(R.layout.fragment_add_receipt, container, false)
        initViews(layout)
        return layout
    }

    private fun initViews(layout: View) {
        nameEditText = layout.findViewById(R.id.nameEditText)
        costEditText = layout.findViewById(R.id.costEditText)
        addPersonEditText = layout.findViewById(R.id.addPersonEditText)
        chipGroup = layout.findViewById(R.id.chipGroup)
        receiptPhoto = layout.findViewById(R.id.receipt_photo)

        val peopleNames = tripWithPeople?.people?.map { it.nickname }

        val adapter: ArrayAdapter<String>? = peopleNames?.let {
            ArrayAdapter<String>(
                requireContext(),
                android.R.layout.simple_dropdown_item_1line,
                it
            )
        }


        addPersonEditText.setAdapter(adapter)
        addPersonEditText.apply {
            setOnFocusChangeListener { _, hasFocus->
                if (hasFocus) {
                    showDropDown()
                }
            }
            setOnEditorActionListener { v, actionId, event ->
                if (actionId == EditorInfo.IME_ACTION_DONE &&
                    peopleNames?.find{ text.toString().toLowerCase() == it.toLowerCase() } != null) {
                    // TODO deal with what to do if person is not already in group
                    val name = text.toString().capitalize()
                    val chip = Chip(context)
                    tripWithPeople?.people?.find { it.nickname.capitalize() == name }
                        ?.let { addedPeople.add(it) }
                    chip.text = name
                    chipGroup.addView(chip)
                    setText("")
                }
                true
            }
        }

        receiptPhoto.setOnLongClickListener {
            submitNewTransaction()
            true
        }
    }

    fun submitNewTransaction() {
        //TODO deal with wrong number of decimal places
        val price = costEditText.text.toString().toFloatOrNull()
        val name = nameEditText.text.toString()
        if (price != null) {
            val newTransaction = transactionViewModel.insertAsync(
                Transaction(name, Date(), tripId, false, price, null),
                addedPeople
            )
            listener?.onReceiptFragmentBackButtonPressed(tripId)
        }
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is AddReceiptFragmentListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement AddReceiptFragmentListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    interface AddReceiptFragmentListener {
        fun onReceiptFragmentBackButtonPressed(tripId: Long)
    }

    companion object {
        private const val TRIP_ID = "tripId"

        @JvmStatic
        fun newInstance(tripId: Long) =
            AddReceiptFragment().apply {
                arguments = Bundle().apply {
                    putLong(TRIP_ID, tripId)
                }
            }
    }
}