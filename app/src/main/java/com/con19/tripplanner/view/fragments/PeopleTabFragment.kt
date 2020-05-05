package com.con19.tripplanner.view.fragments


import android.content.res.TypedArray
import android.graphics.drawable.InsetDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.con19.tripplanner.R
import com.con19.tripplanner.entities.Person
import com.con19.tripplanner.view.adapters.PeopleAdapter

/**
 * Fragment for the Settings Tab.
 */
class PeopleTabFragment : Fragment() {

    private val dataset = listOf(
        Person("Conor", "0273785420"),
        Person("Nick", "24532"),
        Person("Angus", "4323432"),
        Person("Charlotte", "0273785420"),
        Person("James", "24532"),
        Person("Ted", "4323432"),
        Person("Sam", "4323432")
    )
    private lateinit var viewManager: LinearLayoutManager


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val layout: View = inflater.inflate(R.layout.fragment_people_tab, container, false)

        viewManager = LinearLayoutManager(requireContext())
        val recyclerView: RecyclerView = layout.findViewById(R.id.recyclerView)
        val peopleAdapter = PeopleAdapter(dataset)

        recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = peopleAdapter
            setDividers()
        }

        return layout
    }

    /**
     * Set Decorations for recyclerView
     */
    private fun RecyclerView.setDividers() {
        val attrs = intArrayOf(android.R.attr.listDivider)
        val a: TypedArray = context.obtainStyledAttributes(attrs)
        val divider = a.getDrawable(0)
        val inset = 32 // dp
        val insetDivider = InsetDrawable(divider, inset, 0, inset, 0)
        a.recycle()

        val dividerItemDecoration = DividerItemDecoration(
            this.context,
            viewManager.orientation
        )
        dividerItemDecoration.setDrawable(insetDivider)
        this.addItemDecoration(dividerItemDecoration)

    }
}
