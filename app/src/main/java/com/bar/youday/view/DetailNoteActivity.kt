package com.bar.youday.view

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.bar.youday.R
import com.bar.youday.data.Note
import com.bar.youday.data.NoteDatabase
import com.bar.youday.data.repository.NotesRepositoryImp
import com.bar.youday.viewmodel.NotesViewModel
import com.bar.youday.viewmodel.NotesViewModelFactory
import kotlinx.android.synthetic.main.activity_detail_note.*

lateinit var note: Note

class DetailNoteActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_note)

        editToolbar.title = ""
        setSupportActionBar(findViewById(R.id.editToolbar))

        if (intent.hasExtra("note"))
            note = intent.getParcelableExtra<Note>(("note"))!!

        editNoteTitle.setText(note.title)
        editNoteText.setText(note.text)

    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {

        menuInflater.inflate(R.menu.edit_menu, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.edit -> {
                editToolbar.menu.findItem(R.id.edit).isVisible = false
                editToolbar.menu.findItem(R.id.save).isVisible = true

                editNoteTitle.isEnabled = true
                editNoteText.isEnabled = true
            }
            R.id.save -> {
                editToolbar.menu.findItem(R.id.save).isVisible = false
                editToolbar.menu.findItem(R.id.edit).isVisible = true

                editNoteTitle.isEnabled = false
                editNoteText.isEnabled = false

                saveEditNote()

            }
        }

        return false
    }


    private fun saveEditNote() {
        note.text = editNoteText.text.toString()
        note.title = editNoteTitle.text.toString()

        val returnIntent = Intent()
        returnIntent.putExtra("result", note)
        setResult(RESULT_OK, returnIntent)
    }

}

