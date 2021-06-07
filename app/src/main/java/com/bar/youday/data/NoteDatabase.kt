package com.bar.youday.data

import android.content.Context
import androidx.room.*

@Database(
    entities = [Note::class],
    version = 1,
    exportSchema = false
)
//@TypeConverters(NoteTypeConverters::class)
abstract class NoteDatabase : RoomDatabase() {

    abstract fun notesDao(): NotesDao


}