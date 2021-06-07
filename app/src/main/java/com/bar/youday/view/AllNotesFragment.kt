package com.bar.youday.view

import android.content.Intent
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.bar.youday.R
import com.bar.youday.adapter.NoteAdapter
import com.bar.youday.data.Note
import com.bar.youday.data.NotesDao
import com.bar.youday.data.repository.NotesRepositoryImp
import com.bar.youday.helper.Constant
import com.bar.youday.viewmodel.NotesViewModel
import com.bar.youday.viewmodel.NotesViewModelFactory
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_all_notes.*

interface OnItemClick {
    fun onClick(note: Note)
}

class AllNotesFragment : Fragment(), OnItemClick {


    private lateinit var navController: NavController
    private lateinit var notesViewModel: NotesViewModel
    private lateinit var adapter: NoteAdapter
    private lateinit var repository: NotesRepositoryImp
    private lateinit var deleteIcon: Drawable

    private var swipeBackground: ColorDrawable = ColorDrawable(
        Color.parseColor("#FF0000")
    )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_all_notes, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        deleteIcon = context?.let { ContextCompat.getDrawable(it, R.drawable.ic_delete) }!!
        repository = NotesRepositoryImp.get()
        notesViewModel = NotesViewModelFactory(repository).create(NotesViewModel::class.java)
        adapter = NoteAdapter(this)
        recyclerViewNote.layoutManager = StaggeredGridLayoutManager(2, RecyclerView.VERTICAL)
        recyclerViewNote.adapter = adapter
        navController = Navigation.findNavController(view)
        view.findViewById<FloatingActionButton>(R.id.buttonNewNote).setOnClickListener {
            navController.navigate(R.id.action_allNotesFragment_to_newNoteFragment)
        }

        getData()
        onSwipeListener()
        Log.i("resume", "create")


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
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {

                    return false
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
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
                    viewHolder: RecyclerView.ViewHolder,
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
                            itemView.left + iconMargin / 4,
                            itemView.top + iconMargin,
                            itemView.left + iconMargin / 4 + deleteIcon.intrinsicWidth,
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
                            itemView.right - iconMargin / 4 - deleteIcon.intrinsicWidth,
                            itemView.top + iconMargin,
                            itemView.right - iconMargin / 4,
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
        notesViewModel.noteList.observe(viewLifecycleOwner, {
//            typeChoicer(navigation.selectedItemId)
            Log.i("resume", adapter.itemCount.toString())
        })
    }

    fun typeChoicer(type: Int) {

        val i = when (type) {
            R.id.notes -> 0
            R.id.shop -> 1
            R.id.plans -> 2
            else -> 3
        }

        val newList = if (i != 3)
            notesViewModel.noteList.value?.filter { note -> note.type == i }
        else
            notesViewModel.noteList.value


        if (newList != null) {
            adapter.notesList = newList
        }
    }

    override fun onClick(note: Note) {
        val bundle = bundleOf("argNote" to note)
        navController?.navigate(R.id.action_allNotesFragment_to_newNoteFragment, bundle)
    }


}