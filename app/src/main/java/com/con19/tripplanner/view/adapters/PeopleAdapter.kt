package com.con19.tripplanner.view.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.con19.tripplanner.R
import com.con19.tripplanner.db.entities.Person
import kotlinx.android.synthetic.main.view_people_card.view.*

class PeopleAdapter internal constructor(
    context: Context
) : RecyclerView.Adapter<PeopleAdapter.PersonViewHolder>(){

    private val inflater: LayoutInflater = LayoutInflater.from(context)

    var peopleList = emptyList<Person>() // Cached copy of people
        set(person) {
            field = person
            notifyDataSetChanged()
        }


    class PersonViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.nameTextView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PersonViewHolder {
        val peopleCard = inflater.inflate(R.layout.view_people_card, parent, false)
        return PersonViewHolder(peopleCard)
    }

    override fun getItemCount(): Int = peopleList.size

    override fun onBindViewHolder(holder: PersonViewHolder, position: Int) {
        val currentPerson = peopleList[position]
        holder.name.text = currentPerson.nickname
    }

}

// class WordListAdapter internal constructor(
//        context: Context
//) : RecyclerView.Adapter<WordListAdapter.WordViewHolder>() {
//
//    inner class WordViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        val wordItemView: TextView = itemView.findViewById(R.id.textView)
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordViewHolder {
//        val itemView = inflater.inflate(R.layout.recyclerview_item, parent, false)
//        return WordViewHolder(itemView)
//    }
//
//    override fun onBindViewHolder(holder: WordViewHolder, position: Int) {
//        val current = words[position]
//        holder.wordItemView.text = current.word
//    }
//
//    internal fun setWords(words: List<Word>) {
//        this.words = words
//        notifyDataSetChanged()
//    }
//
//    override fun getItemCount() = words.size
//}
