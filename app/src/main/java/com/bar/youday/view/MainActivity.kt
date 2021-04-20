package com.bar.youday.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.VERTICAL
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.bar.youday.R
import com.bar.youday.adapter.NoteAdapter
import com.bar.youday.data.Note
import com.bar.youday.data.NoteDatabase
import com.bar.youday.data.NotesDao
import com.bar.youday.data.repository.NotesRepositoryImp
import com.bar.youday.viewmodel.NotesViewModel
import com.bar.youday.viewmodel.NotesViewModelFactory
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*

interface OnItemClick {
    fun onClick(note: Note)
}

class MainActivity : AppCompatActivity(), OnItemClick {
    //todo сделать плвный приятный дизайн по типу как на самсунг

    private lateinit var dao: NotesDao
    private lateinit var notesViewModel: NotesViewModel
    private lateinit var adapter: NoteAdapter
    private lateinit var repository: NotesRepositoryImp

    private val NEW_NOTE_ACTIVITY = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dao = NoteDatabase.invoke(this).notesDao()
        repository = NotesRepositoryImp(dao)
        notesViewModel = NotesViewModelFactory(repository).create(NotesViewModel::class.java)
        adapter = NoteAdapter(this, this)
        recyclerViewNote.layoutManager = StaggeredGridLayoutManager(2, VERTICAL)
        recyclerViewNote.adapter = adapter

        getData()
        onSwipeListener()
        Log.i("resume", "create")



        navigation.setOnNavigationItemSelectedListener { item ->
            var newList: List<Note>? = null

            val i = when (item.itemId) {
                R.id.notes -> 0
                R.id.shop -> 1
                R.id.plans -> 2
                else -> 3
            }

            newList = if (i != 3)
                notesViewModel.noteList.value?.filter { note -> note.type == i }
            else
                notesViewModel.noteList.value


            if (newList != null) {
                adapter.notesList = newList
            }

            true
        }

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
            Log.i("resume", adapter.itemCount.toString())
        })
    }


    private fun obs(int: Int) {


    }

    fun addNote(view: View) {
        val intent = Intent(this, NewNoteActivity::class.java)
        startActivityForResult(intent, NEW_NOTE_ACTIVITY)
    }

    override fun onClick(note: Note) {
        val intent = Intent(this, DetailNoteActivity::class.java)
        intent.putExtra("note", note)
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
