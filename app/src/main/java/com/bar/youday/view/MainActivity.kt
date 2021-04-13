package com.bar.youday.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bar.youday.R
import com.bar.youday.adapter.NoteAdapter
import com.bar.youday.data.Note
import com.bar.youday.data.NoteDatabase
import com.bar.youday.data.repository.NotesRepositoryImp
import com.bar.youday.viewmodel.NotesViewModel
import com.bar.youday.viewmodel.NotesViewModelFactory

class MainActivity : AppCompatActivity() {

    private lateinit var notesViewModel: NotesViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: NoteAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val dao = NoteDatabase.invoke(this).notesDao()
        val repository = NotesRepositoryImp(dao)
        notesViewModel = NotesViewModelFactory(repository).create(NotesViewModel::class.java)

        adapter = NoteAdapter(mutableListOf())
        recyclerView = findViewById(R.id.recyclerViewNote)
        recyclerView.layoutManager = LinearLayoutManager(this)

        recyclerView.adapter = adapter

        notesViewModel.noteList.observe(this, {
            it.let {
                adapter = NoteAdapter(it)
            }
                adapter.notifyDataSetChanged()
                Log.i("adapt",adapter.itemCount.toString())
        })
    }

    fun addNote(view: View) {
        val intent = Intent(this, NewNoteActivity::class.java)
        startActivity(intent);
    }
}