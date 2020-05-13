package com.con19.tripplanner.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.con19.tripplanner.R
import com.con19.tripplanner.db.entities.Person
import com.con19.tripplanner.viewmodel.PersonViewModel
import com.google.android.material.textfield.TextInputLayout

private const val PERSON_ID = "personid"

class EditPersonFragment : Fragment() {
    private var personId: Long? = null
    private lateinit var personViewModel: PersonViewModel
    private var person: Person? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            personId = it.getLong(PERSON_ID)
        }
        personViewModel = ViewModelProvider(requireActivity())
            .get(PersonViewModel::class.java)

        person = personId?.let { personViewModel.getPersonById(it) }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val layout: View = inflater.inflate(R.layout.fragment_edit_person, container, false)

        layout.findViewById<TextInputLayout>(R.id.nameTextInput).editText?.apply {
            setText(person?.nickname)

        }

        layout.findViewById<TextInputLayout>(R.id.numberTextInput).editText?.apply {
            setText(person?.phoneNumber)

        }
        return layout
    }

    companion object {
        @JvmStatic
        fun newInstance(personId: Long) =
            EditPersonFragment().apply {
                arguments = Bundle().apply {
                    putLong(PERSON_ID, personId)
                }
            }
    }

}