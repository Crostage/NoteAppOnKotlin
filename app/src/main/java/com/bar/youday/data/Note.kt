package com.bar.youday.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "all_notes")
class Note(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    title: String = "",
    text: String = "",
    type: Int = 0,
    date: Date = Date()
)

