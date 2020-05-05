package com.con19.tripplanner.view.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.con19.tripplanner.R
import com.con19.tripplanner.entities.Person
import kotlinx.android.synthetic.main.view_people_card.view.*

class PeopleAdapter(private val dataset: List<Person>) :
    RecyclerView.Adapter<PeopleAdapter.PersonViewHolder>(){

    class PersonViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.nameTextView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PersonViewHolder {
        val peopleCard = LayoutInflater.from(parent.context)
            .inflate(R.layout.view_people_card, parent, false)
        return PersonViewHolder(peopleCard)
    }

    override fun getItemCount(): Int = dataset.size

    override fun onBindViewHolder(holder: PersonViewHolder, position: Int) {
        holder.name.text = dataset[position].nickname
    }

}
