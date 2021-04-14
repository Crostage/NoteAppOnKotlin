package com.bar.youday.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bar.youday.R
import com.bar.youday.data.Note

class NoteAdapter() : RecyclerView.Adapter<NoteViewHolder>() {

    var notesList: List<Note> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.note_item, parent, false)
        return NoteViewHolder(view)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note = notesList[position]
        holder.bind(note)
    }

    override fun getItemCount(): Int {
        return notesList.size
    }


}


class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private var itemTitle: TextView? = null
    private var itemText: TextView? = null


    init {
        itemTitle = itemView.findViewById(R.id.itemTitle)
        itemText = itemView.findViewById(R.id.itemText)
    }

    fun bind(note: Note) {
        itemTitle?.text = note.title
        itemText?.text = note.text
        Log.i("bind", "1")
    }
}