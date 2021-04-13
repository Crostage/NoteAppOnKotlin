package com.bar.youday.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface NotesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: Note)

    @Delete
    fun removeNote(note: Note)

    @Query("DELETE FROM all_notes")
    fun removeAllNotes()

    @Query("SELECT*FROM all_notes ORDER BY id ")
    fun getNoteList(): LiveData<List<Note>>

    @Query("DELETE FROM all_notes WHERE id = :id")
    fun deleteNoteById(id: Int)
}