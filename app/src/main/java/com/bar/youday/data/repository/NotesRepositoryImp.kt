package com.bar.youday.data.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Room
import com.bar.youday.data.Note
import com.bar.youday.data.NoteDatabase
import java.lang.IllegalStateException

class NotesRepositoryImp private constructor(context: Context) : NotesRepository {


    companion object {
        private var instance: NotesRepositoryImp? = null
        private const val DATABASE_NAME = "notes.db"

        fun initialize(context: Context) {
            if (instance == null) {
                instance = NotesRepositoryImp(context)
            }
        }

        fun get(): NotesRepositoryImp {
            return instance ?: throw  IllegalStateException("NoteRepository must be initialized")
        }


    }

    private val database: NoteDatabase = Room.databaseBuilder(
        context.applicationContext,
        NoteDatabase::class.java,
        DATABASE_NAME
    ).build()

    private val notesDao = database.notesDao()

    override  fun getNoteList(): LiveData<List<Note>> {
        return notesDao.getNoteList()
    }

    override suspend fun insertNote(note: Note) {
        notesDao.insertNote(note)
    }

    override suspend fun updateNote(note: Note) {
        notesDao.updateNote(note)
    }

    override suspend  fun removeNote(note: Note) {
        notesDao.removeNote(note)
    }

    override  suspend fun removeAll() {
        notesDao.removeAllNotes()
    }

    override suspend fun deleteNoteById(id: Int) {
        notesDao.deleteNoteById(id)
    }

}