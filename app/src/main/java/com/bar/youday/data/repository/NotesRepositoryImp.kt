package com.bar.youday.data.repository

import androidx.lifecycle.LiveData
import com.bar.youday.data.Note
import com.bar.youday.data.NotesDao

class NotesRepositoryImp( private val notesDao: NotesDao):NotesRepository {

    override  fun getNoteList(): LiveData<List<Note>> {
        return notesDao.getNoteList()
    }

    override suspend fun insertNote(note: Note) {
      notesDao.insertNote(note)
    }

    override  fun removeNote(note: Note) {
        notesDao.removeNote(note)
    }

    override  fun removeAll() {
        notesDao.removeAllNotes()
    }

    override  fun deleteNoteById(id: Int) {
        notesDao.deleteNoteById(id)
    }
}