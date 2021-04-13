package com.bar.youday.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.bar.youday.R
import com.bar.youday.data.Note
import com.bar.youday.data.NoteDatabase
import com.bar.youday.data.repository.NotesRepositoryImp
import com.bar.youday.viewmodel.NotesViewModel
import com.bar.youday.viewmodel.NotesViewModelFactory
import kotlinx.android.synthetic.main.activity_new_note.*

class NewNoteActivity : AppCompatActivity() {

    private lateinit var notesViewModel: NotesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_note)

        val dao = NoteDatabase.invoke(this).notesDao()
        val repository = NotesRepositoryImp(dao)
        notesViewModel = NotesViewModelFactory(repository).create(NotesViewModel::class.java)
    }

    fun addNote(view: View) {
        val note = newNote()
        notesViewModel.insert(note)
    }

    private fun newNote(): Note {
        val title = titleNote.text.toString()
        val text = textNote.text.toString()

        val radio = when (radioGroup.checkedRadioButtonId) {
            R.id.radioBuy -> 1
            R.id.radioPlans -> 2
            else -> 0
        }
        //todo разобратсья с id
        return Note(title, text, radio)
    }
}