package com.con19.tripplanner.view.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.con19.tripplanner.R
import com.con19.tripplanner.db.entities.Person
import com.con19.tripplanner.view.activities.MainActivity
import kotlinx.android.synthetic.main.view_basic_card.view.*

class PeopleAdapter internal constructor(
    context: Context
) : RecyclerView.Adapter<PeopleAdapter.PersonViewHolder>(){

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var listener: OnPersonClickedListener? = null

    init {
        if (context is MainActivity) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnPersonClickedListener")
        }
    }

    var peopleList = emptyList<Person>() // Cached copy of people
        set(peopleList) {
            field = peopleList
            notifyDataSetChanged()
        }


    class PersonViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.textView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PersonViewHolder {
        val peopleCard = inflater.inflate(R.layout.view_basic_card, parent, false)
        return PersonViewHolder(peopleCard)
    }

    override fun getItemCount(): Int = peopleList.size

    override fun onBindViewHolder(holder: PersonViewHolder, position: Int) {
        val currentPerson = peopleList[position]

        holder.apply {
            name.text = currentPerson.nickname
            view.setOnClickListener {
                listener?.onPersonClicked(currentPerson.personId)
            }
        }
    }

    interface OnPersonClickedListener {
        fun onPersonClicked(personId: Long)
    }

}