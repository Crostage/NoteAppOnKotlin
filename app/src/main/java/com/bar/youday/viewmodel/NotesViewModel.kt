package com.bar.youday.viewmodel

import androidx.lifecycle.ViewModel
import com.bar.youday.data.Note
import com.bar.youday.data.NoteDatabase

class NotesViewModel : ViewModel() {

    private val db = NoteDatabase.getInstance(this)

    val noteList = db.notesDao().getNoteList() //нужно подписаться

    fun insertNote(note: Note) = db.notesDao().addNote(note)
    fun removeNote(note: Note) = db.notesDao().removeNote(note)
    fun removeAll() = db.notesDao().removeAllNotes()


}