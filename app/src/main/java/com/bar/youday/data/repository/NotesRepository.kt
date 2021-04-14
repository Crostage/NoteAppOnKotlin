package com.bar.youday.data.repository

import androidx.lifecycle.LiveData
import com.bar.youday.data.Note

interface NotesRepository {

    fun getNoteList(): LiveData<List<Note>>
    suspend fun insertNote(note: Note)
    suspend fun removeNote(note: Note)
    suspend fun removeAll()
    suspend fun deleteNoteById(id: Int)

}