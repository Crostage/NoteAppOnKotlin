package com.bar.youday.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface NotesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addNote(note: Note)

    @Delete
    fun removeNote(note: Note)

    @Query("DELETE FROM all_notes")
    fun removeAllNotes()

    @Query("SELECT*FROM all_notes ORDER BY id ")
    fun getNoteList(): LiveData<List<Note>>
}