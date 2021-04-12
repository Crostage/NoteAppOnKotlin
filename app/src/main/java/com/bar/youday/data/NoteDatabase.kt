package com.bar.youday.data

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.Database

@Database(entities = [Note::class],version = 1,exportSchema = false)
abstract class NoteDatabase : RoomDatabase() {
    companion object {
        private var db: NoteDatabase? = null
        private const val DB_NAME = "notes.db"
        private val LOCK = Any()

        fun getInstance(context: Context): NoteDatabase {
            synchronized(LOCK) {
                db?.let { return it }  // db!=null
                return Room.databaseBuilder(context, NoteDatabase::class.java, DB_NAME)
                    .build() // db == null
            }
        }
    }

    abstract fun notesDao() : NotesDao
}