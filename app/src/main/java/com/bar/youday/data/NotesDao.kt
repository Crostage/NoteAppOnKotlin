package com.bar.youday.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface  NotesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: Note)

    @Update
    suspend fun updateNote(note: Note)

    @Delete
    suspend fun removeNote(note: Note)

    @Query("DELETE FROM all_notes")
    suspend fun removeAllNotes()

    @Query("SELECT*FROM all_notes ORDER BY id DESC ")
    fun getNoteList(): LiveData<List<Note>>

    @Query("DELETE FROM all_notes WHERE id = :id")
    suspend fun deleteNoteById(id: Int)
}