package com.bar.youday.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bar.youday.R
import com.bar.youday.data.Note
import com.bar.youday.view.DetailNoteActivity

class NoteAdapter(private val context: Context) : RecyclerView.Adapter<NoteViewHolder>() {

    var notesList: List<Note> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.note_item, parent, false)
        return NoteViewHolder(view)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note = notesList[position]
        holder.bind(note)
        holder.itemView.setOnClickListener {
            Toast.makeText(context, "", Toast.LENGTH_SHORT).show()
            val intent = Intent(context, DetailNoteActivity::class.java)
            intent.putExtra("note",note)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return notesList.size
    }
}

class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private var itemTitle: TextView? = null
    private var itemText: TextView? = null
    private var itemDate: TextView? = null

    init {
        itemTitle = itemView.findViewById(R.id.itemTitle)
        itemText = itemView.findViewById(R.id.itemText)
        itemDate = itemView.findViewById(R.id.itemDate)
    }

    fun bind(note: Note) {
        itemTitle?.text = note.title
        itemText?.text = note.text
        itemDate?.text = note.date
    }
}