package com.bar.youday.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "all_notes")
class Note(
    var title: String = "",
    var text: String = "",
    var type: Int = 0,
    var date: String = Date().toString()
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}

