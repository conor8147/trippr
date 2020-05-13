package com.con19.tripplanner.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.con19.tripplanner.R

private const val PERSON_ID = "personid"

class EditPersonFragment: Fragment() {
    private var personId: Long? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            personId = it.getLong(PERSON_ID)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val layout: View = inflater.inflate(R.layout.fragment_edit_person, container, false)
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