package com.bar.youday.view

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import com.bar.youday.R
import com.bar.youday.data.Note
import com.bar.youday.data.repository.NotesRepositoryImp
import com.bar.youday.helper.Constant
import com.bar.youday.viewmodel.NotesViewModel
import com.bar.youday.viewmodel.NotesViewModelFactory
import kotlinx.android.synthetic.main.activity_new_note.*
import java.io.ByteArrayOutputStream


class NewNoteActivity : AppCompatActivity(),Sebkabl {

    private lateinit var notesViewModel: NotesViewModel
    private var pointFlag = false
    var imageByteArray: ByteArray? = null
    var note: Note? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_note)


        newNoteBar.title = ""
        setSupportActionBar(findViewById(R.id.newNoteBar))


        if (intent.hasExtra(Constant.NOTE_EXTRA)) {
            note = intent.getParcelableExtra((Constant.NOTE_EXTRA))!!
            note?.let {
                titleNote.setText(it.title)
                textNote.setText(it.text)

                it.image?.let {
                    val image = BitmapFactory.decodeByteArray(it, 0, it.size)

                    noteImg.setImageBitmap(image)
                }
                val type = when (it.type) {
                    1 -> R.id.radioBuy
                    2 -> R.id.radioPlans
                    else -> R.id.radioNotes
                }
                radioGroup.check(type)
            }

        }


        val repository = NotesRepositoryImp.get()
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
                startActivityForResult(photoPickerIntent, Constant.LOAD_IMG_RESULT)
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
            Toast.makeText(this, R.string.Toast_attention_message, Toast.LENGTH_SHORT).show()
            return
        }

        val returnIntent = Intent()
        returnIntent.putExtra(Constant.INTENT_RESULT, note)
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

        val newNote = Note(title, text, radio)

        note?.let { n ->

            newNote.id = n.id
            go(5)

        }

        imageByteArray?.let {
            newNote.image = it
        }
        return newNote
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Constant.LOAD_IMG_RESULT && resultCode == RESULT_OK) {
            val uri = data?.data
            val impStr = uri?.let { contentResolver.openInputStream(it) }
            val stream = ByteArrayOutputStream()
            val imageBitmap = BitmapFactory.decodeStream(impStr)

            imageBitmap.compress(Bitmap.CompressFormat.PNG, 90, stream)
            imageByteArray = stream.toByteArray()
            noteImg.setImageBitmap(imageBitmap)
        }
    }

    fun addNote(view: View) {}


}

interface Sebkabl {
    fun go(int: Int): Int {
        return int*2
    }

    companion object{
        fun stop(double: Double): Double{
            return double*0
        }
    }
}