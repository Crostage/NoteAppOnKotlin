package com.bar.youday.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.GridLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.VERTICAL
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.bar.youday.R
import com.bar.youday.adapter.NoteAdapter
import com.bar.youday.data.Note
import com.bar.youday.data.NoteDatabase
import com.bar.youday.data.repository.NotesRepositoryImp
import com.bar.youday.viewmodel.NotesViewModel
import com.bar.youday.viewmodel.NotesViewModelFactory
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    //todo сделать плвный приятный дизайн по типу как на самсунг

    private lateinit var notesViewModel: NotesViewModel
    private lateinit var adapter: NoteAdapter

    private val NEW_NOTE_ACTIVITY = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val dao = NoteDatabase.invoke(this).notesDao()
        val repository = NotesRepositoryImp(dao)
        notesViewModel = NotesViewModelFactory(repository).create(NotesViewModel::class.java)
        adapter = NoteAdapter(this)
        recyclerViewNote.layoutManager = StaggeredGridLayoutManager(2, VERTICAL)
        recyclerViewNote.adapter = adapter

        getData()
        onSwipeListener()
    }

    private fun onSwipeListener() {
        val itemTouchHelperCallback =
            object :
                ItemTouchHelper.SimpleCallback(
                    0,
                    ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
                ) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: ViewHolder,
                    target: ViewHolder
                ): Boolean {

                    return false
                }

                override fun onSwiped(viewHolder: ViewHolder, direction: Int) {
                    notesViewModel.remove(adapter.notesList[viewHolder.adapterPosition])
                    Toast.makeText(
                        this@MainActivity,
                        "заметка удалена",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            }

        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(recyclerViewNote)
    }

    private fun getData() {
        notesViewModel.noteList.observe(this, {
            adapter.notesList = it
            Log.i("adapt", adapter.itemCount.toString())
        })
    }

    fun addNote(view: View) {
        val intent = Intent(this, NewNoteActivity::class.java)
        startActivityForResult(intent, NEW_NOTE_ACTIVITY)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == NEW_NOTE_ACTIVITY) {
            if (resultCode == Activity.RESULT_OK) {
                val note = data?.getParcelableExtra<Note>("result")
                note?.let { notesViewModel.insert(it) }
            }
        }
    }
}
