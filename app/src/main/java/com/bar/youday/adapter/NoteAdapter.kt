package com.bar.youday.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bar.youday.R
import com.bar.youday.data.Note
import com.bar.youday.view.OnItemClick


class NoteAdapter(private val context: Context, private val callback: OnItemClick) : RecyclerView.Adapter<NoteViewHolder>() {

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
            callback.onClick(note)
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
    private var itemImg: ImageView? = null

    init {
        itemTitle = itemView.findViewById(R.id.itemTitle)
        itemText = itemView.findViewById(R.id.itemText)
        itemDate = itemView.findViewById(R.id.itemDate)
        itemImg = itemView.findViewById(R.id.itemImg)
    }

    fun bind(note: Note) {
        itemTitle?.text = note.title
        itemText?.text = note.text
        itemDate?.text = note.date

        when(note.type){
            1-> itemImg?.setImageResource(R.drawable.shopping)
            2-> itemImg?.setImageResource(R.drawable.pencil)
            else -> itemImg?.setImageResource(R.drawable.notes)
        }
    }
}