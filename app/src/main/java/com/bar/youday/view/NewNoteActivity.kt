package com.bar.youday.view

import android.content.Intent
import android.os.Bundle
import android.text.Selection.setSelection
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import androidx.core.widget.doOnTextChanged
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

        var flag = 0
        textNote.doOnTextChanged { text, _, _, _
            ->
            if(flag == 0) {
                textNote.setText(text.toString().replace("\n", "\n• "))
                flag = 1
            }

        }

    }

    fun addNote(view: View) {
        val note = newNote()

        if (note.title.isEmpty() || note.text.isEmpty()) {
            Toast.makeText(this, "Заполните все поля", Toast.LENGTH_SHORT).show()
            return
        }

        val returnIntent = Intent()
        returnIntent.putExtra("result", note)
        setResult(RESULT_OK, returnIntent)
        finish()
    }

    private fun newNote(): Note {
        val title = titleNote.text.toString()
        val text = textNote.text.toString()

        val radio = when (radioGroup.checkedRadioButtonId) {
            R.id.radioBuy -> 1
            R.id.radioPlans -> 2
            else -> 0
        }
        return Note(title, text, radio)
    }

}