package com.con19.tripplanner.view.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider

import com.con19.tripplanner.R
import com.con19.tripplanner.db.entities.Person
import com.con19.tripplanner.viewmodel.PersonViewModel
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.textfield.TextInputLayout
import java.lang.RuntimeException

class AddPersonFragment : Fragment() {
    private var listener: AddPersonFragmentListener? = null
    private var personId: Long? = null
    private lateinit var personViewModel: PersonViewModel
    private var person: Person? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        personViewModel = ViewModelProvider(requireActivity())
            .get(PersonViewModel::class.java)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val layout = inflater.inflate(R.layout.fragment_edit_person, container, false)
        val toolbar = layout.findViewById<MaterialToolbar>(R.id.topAppBar)
        val nameTextInput = layout.findViewById<TextInputLayout>(R.id.nameTextInput)
        val numberTextInput = layout.findViewById<TextInputLayout>(R.id.numberTextInput)

        toolbar.apply {
            title = getString(R.string.add_person)
            setNavigationOnClickListener {
                listener?.onFinished()
            }
            setOnMenuItemClickListener {
                if (it.itemId == R.id.add) {
                    addPerson(
                        nameTextInput.editText?.text.toString(),
                        numberTextInput.editText?.text.toString()
                    )
                }
                true
            }
        }
        return layout
    }

    private fun addPerson(name: String, number: String) {
        personViewModel.insertAsync(Person(name, number))
        listener?.onFinished()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is AddPersonFragmentListener) {
            this.listener = context
        } else {
            throw RuntimeException("$context must implement AddPersonFragmentListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        this.listener = null
    }

    interface AddPersonFragmentListener {
        fun onFinished()
    }
}
