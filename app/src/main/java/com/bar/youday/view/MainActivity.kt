package com.bar.youday.view

import android.app.Activity
import android.content.Intent
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
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
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*

interface OnItemClick {
    fun onClick(note: Note)
}

class MainActivity : AppCompatActivity(), OnItemClick {
    //todo сделать плвный приятный дизайн по типу как на самсунг

    private lateinit var dao: NotesDao
    private lateinit var notesViewModel: NotesViewModel
    private lateinit var adapter: NoteAdapter
    private lateinit var repository: NotesRepositoryImp
    private lateinit var deleteIcon: Drawable
    private lateinit var toast: Toast
    private var swipeBackground: ColorDrawable = ColorDrawable(
        Color
            .parseColor("#FF0000")
    )

    private val RESULT = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        deleteIcon = ContextCompat.getDrawable(this, R.drawable.ic_delete)!!
        dao = NoteDatabase.invoke(this).notesDao()
        repository = NotesRepositoryImp(dao)
        notesViewModel = NotesViewModelFactory(repository).create(NotesViewModel::class.java)
        adapter = NoteAdapter(this, this)
        recyclerViewNote.layoutManager = StaggeredGridLayoutManager(2, VERTICAL)
        recyclerViewNote.adapter = adapter

        getData()
        onSwipeListener()
        Log.i("resume", "create")

        navigation.itemIconTintList = null
        navigation.setOnNavigationItemSelectedListener { item ->
            typeChoice(item.itemId)

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
                    val note = adapter.notesList[viewHolder.adapterPosition]
                    notesViewModel.remove(note)

                    Snackbar.make(
                        viewHolder.itemView,
                        "${note.title} удалена",
                        Snackbar.LENGTH_SHORT
                    ).setAction("Undo") {
                        notesViewModel.insert(note)
                    }.show()
                }

                override fun onChildDraw(
                    c: Canvas,
                    recyclerView: RecyclerView,
                    viewHolder: ViewHolder,
                    dX: Float,
                    dY: Float,
                    actionState: Int,
                    isCurrentlyActive: Boolean
                ) {

                    val itemView = viewHolder.itemView

                    val iconMargin = (itemView.height - deleteIcon.intrinsicHeight) / 2

                    if (dX > 0) {
                        swipeBackground
                            .setBounds(itemView.left, itemView.top, dX.toInt(), itemView.bottom)
                        deleteIcon.setBounds(
                            itemView.left + iconMargin/4,
                            itemView.top + iconMargin,
                            itemView.left + iconMargin/4 + deleteIcon.intrinsicWidth,
                            itemView.bottom - iconMargin
                        )

                    } else {
                        swipeBackground
                            .setBounds(
                                itemView.right + dX.toInt(),
                                itemView.top,
                                itemView.right,
                                itemView.bottom
                            )
                        deleteIcon.setBounds(
                            itemView.right - iconMargin/4 - deleteIcon.intrinsicWidth,
                            itemView.top + iconMargin,
                            itemView.right - iconMargin/4,
                            itemView.bottom - iconMargin
                        )
                    }

                    swipeBackground.draw(c)
                    deleteIcon.draw(c)

                    super.onChildDraw(
                        c,
                        recyclerView,
                        viewHolder,
                        dX,
                        dY,
                        actionState,
                        isCurrentlyActive
                    )
                }
            }

        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(recyclerViewNote)
    }





    private fun getData() {
        notesViewModel.noteList.observe(this, {
            typeChoice(navigation.selectedItemId)
            Log.i("resume", adapter.itemCount.toString())
        })
    }

    fun typeChoice(type: Int) {
        var newList: List<Note>? = null

        val i = when (type) {
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
    }


    fun addNote(view: View) {
        val intent = Intent(this, NewNoteActivity::class.java)
        startActivityForResult(intent, RESULT)
    }

    override fun onClick(note: Note) {
        val intent = Intent(this, DetailNoteActivity::class.java)
        intent.putExtra("note", note)
        startActivityForResult(intent, RESULT)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RESULT) {
            if (resultCode == Activity.RESULT_OK) {
                val note = data?.getParcelableExtra<Note>("result")
                note?.let { notesViewModel.insert(it) }
            }
        }
    }
}
