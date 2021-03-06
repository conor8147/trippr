package com.con19.tripplanner.view.fragments

import android.content.Context
import android.content.res.TypedArray
import android.graphics.drawable.InsetDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.con19.tripplanner.R
import com.con19.tripplanner.view.adapters.PeopleAdapter
import com.con19.tripplanner.viewmodel.PersonViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.lang.RuntimeException

/**
 * Fragment for the Settings Tab.
 */
class PeopleHomeFragment : Fragment() {
    private var listener: PeopleHomeFragmentListener? = null

    private lateinit var viewManager: LinearLayoutManager
    private lateinit var personViewModel: PersonViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        personViewModel = ViewModelProvider(requireActivity())
            .get(PersonViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val layout: View = inflater.inflate(R.layout.fragment_people_home, container, false)
        layout.findViewById<FloatingActionButton>(R.id.floatingActionButton).setOnClickListener {
            listener?.onAddPersonClicked();
        }
        initialiseRecyclerView(layout)
        return layout
    }

    private fun initialiseRecyclerView(layout: View) {
        viewManager = LinearLayoutManager(requireContext())
        val recyclerView: RecyclerView = layout.findViewById(R.id.recyclerView)
        val peopleAdapter = PeopleAdapter(requireContext())

        recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = peopleAdapter
        }

        personViewModel.allPeople.observe(viewLifecycleOwner, Observer { people ->
            // Update the cached copy of the words in the adapter.
            people?.let { peopleAdapter.peopleList = it }
        })
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is PeopleHomeFragmentListener) {
            this.listener = context
        } else {
            throw RuntimeException("$context must implement PeopleHomeFragmentListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        this.listener = null
    }

    interface PeopleHomeFragmentListener {
        fun onAddPersonClicked()
    }
}
