package com.bar.youday.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.bar.youday.R
import com.bar.youday.data.Note
import com.bar.youday.viewmodel.NotesViewModel
import kotlinx.android.synthetic.main.activity_new_note.*

class NewNoteActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_note)
    }

    fun addNote(view: View) {
        val note = newNote()
        NotesViewModel(application).insertNote(note)

    }

    private fun newNote():Note {
        val title = titleNote.text.toString()
        val text = textNote.text.toString()

        val radio = when (radioGroup.checkedRadioButtonId) {
            R.id.radioBuy -> 1
            R.id.radioPlans -> 2
            else -> 0
        }
        //todo разобратсья с id
       return Note(0,title, text, radio)
    }
}