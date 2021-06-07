package com.bar.youday.view

import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bar.youday.R
import com.bar.youday.data.Note
import com.bar.youday.viewmodel.NotesViewModel
import kotlinx.android.synthetic.main.activity_new_note.*

class NewNoteFragment : Fragment() {

    private lateinit var notesViewModel: NotesViewModel
    private var pointFlag = false
    var imageByteArray: ByteArray? = null
    var note: Note? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        note = arguments?.getParcelable("argNote")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_new_note, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



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

    fun addNote() {
        val note = newNote()

        if (note.title.isEmpty() || note.text.isEmpty()) {
            Toast.makeText(context, R.string.Toast_attention_message, Toast.LENGTH_SHORT).show()
            return
        }

    }

    fun newNote(): Note {
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
        }

        imageByteArray?.let {
            newNote.image = it
        }
        return newNote
    }
}