package com.con19.tripplanner.view.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.con19.tripplanner.R
import com.con19.tripplanner.db.entities.Person
import com.con19.tripplanner.viewmodel.PersonViewModel
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.textfield.TextInputLayout
import java.lang.RuntimeException

private const val PERSON_ID = "personid"

class EditPersonFragment : Fragment() {
    private var personId: Long? = null
    private lateinit var personViewModel: PersonViewModel
    private var person: Person? = null
    private var listener: EditPersonFragmentListener? = null

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
        val nameTextInput = layout.findViewById<TextInputLayout>(R.id.nameTextInput)
        val numberTextInput = layout.findViewById<TextInputLayout>(R.id.numberTextInput)
        val toolbar = layout.findViewById<MaterialToolbar>(R.id.topAppBar)

        nameTextInput.editText?.apply {
            setText(person?.nickname)
        }
        numberTextInput.editText?.apply {
            setText(person?.phoneNumber)
        }

        toolbar.apply {
            setNavigationOnClickListener {
                listener?.onFinished()
            }
            setOnMenuItemClickListener {
                if (it.itemId == R.id.add) {
                    updatePerson(
                        nameTextInput.editText?.text.toString(),
                        numberTextInput.editText?.text.toString()
                    )
                }
                true
            }
        }
        return layout
    }

    private fun updatePerson(name: String, number: String) {
        person?.let { personViewModel.updatePerson(it, name, number) }
        listener?.onFinished()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is EditPersonFragmentListener) {
            this.listener = context
        } else {
            throw RuntimeException("$context must implement EditPersonFragmentListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        this.listener = null
    }

    interface EditPersonFragmentListener {
        fun onFinished()
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