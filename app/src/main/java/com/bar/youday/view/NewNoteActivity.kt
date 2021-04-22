package com.bar.youday.view

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import com.bar.youday.R
import com.bar.youday.data.Note
import com.bar.youday.data.NoteDatabase
import com.bar.youday.data.repository.NotesRepositoryImp
import com.bar.youday.viewmodel.NotesViewModel
import com.bar.youday.viewmodel.NotesViewModelFactory
import kotlinx.android.synthetic.main.activity_new_note.*


class NewNoteActivity : AppCompatActivity() {

    private val RESULT_LOAD_IMG = 1
    private lateinit var notesViewModel: NotesViewModel
    private var pointFlag = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_note)


        newNoteBar.title = ""
        setSupportActionBar(findViewById(R.id.newNoteBar))

        val dao = NoteDatabase.invoke(this).notesDao()
        val repository = NotesRepositoryImp(dao)
        notesViewModel = NotesViewModelFactory(repository).create(NotesViewModel::class.java)

    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {

        menuInflater.inflate(R.menu.tools_menu, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {

            R.id.pointTool -> {

                pointFlag = !pointFlag

                if (pointFlag)
                    item.setIcon(R.drawable.ic_point_checked)
                else
                    item.setIcon(R.drawable.ic_point)


                if (textNote.isFocused && pointFlag) {
                    textNote.append("\n• ")
                    textNote.doOnTextChanged { text, _, _, _ ->
                        if (pointFlag)
                            if (text?.endsWith("\n") == true) {
                                textNote.append("• ")
                            }

                    }
                }


            }
            R.id.clipTool -> {
                val photoPickerIntent = Intent(Intent.ACTION_PICK)
                photoPickerIntent.type = "image/*"
                startActivityForResult(photoPickerIntent, RESULT_LOAD_IMG)
            }
            R.id.saveButton -> {
                addNote()

            }
        }

        return false
    }

    private fun addNote() {
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