package com.example.myapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

enum class ButtonClickType{
    EDIT,
    DELETE
}

class PersonRecyclerAdapter(private val items: ArrayList<Person>, val listener:ContentListener): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return PersonViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.recycler_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder){
            is PersonViewHolder -> {
                holder.bind(position, items[position], listener)
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun removeItem(index: Int){
        items.removeAt(index)
        notifyItemRemoved(index)
        notifyItemRangeChanged(index, items.size)
    }
    fun saveItem(person: Person){
        items.add(getItemCount(), person)
        notifyItemInserted(getItemCount())


    }
    class PersonViewHolder(
        private val view: View): RecyclerView.ViewHolder(view) {
        private val personImage =
            view.findViewById<ImageView>(R.id.personImage)
        private val personName =
            view.findViewById<TextView>(R.id.personName)
        private val personDescription =
            view.findViewById<TextView>(R.id.personDescription)
        private val editBtn =
            view.findViewById<ImageButton>(R.id.editButton)
        private val deleteBtn =
            view.findViewById<ImageButton>(R.id.deleteButton)

        fun bind(
            index: Int,
            person: Person,
            listener: ContentListener
        ) {
            Glide.with(view.context).load(person.imageUrl).into(personImage)
            personName.setText(person.name)
            personDescription.setText(person.description)
            editBtn.setOnClickListener {
                person.name = personName.text.toString()
                person.description = personDescription.text.toString()
                listener.onItemButtonClick(index, person, ButtonClickType.EDIT)
            }
            deleteBtn.setOnClickListener {
                listener.onItemButtonClick(index, person, ButtonClickType.DELETE)
            }
        }
    }
    interface ContentListener{
        fun onItemButtonClick(index: Int, item: Person, clickType: ButtonClickType)
    }
}